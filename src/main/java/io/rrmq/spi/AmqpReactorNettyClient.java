package io.rrmq.spi;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.rrmq.spi.decoder.AmqpResponseDecoder;
import io.rrmq.spi.decoder.AmqpResponseReaderDecoder;
import io.rrmq.spi.exception.CloseAmqpConnectionException;
import io.rrmq.spi.method.connection.CloseOk;
import io.rrmq.spi.method.connection.OpenOk;
import io.rrmq.spi.method.connection.impl.CloseAmqpMethod;
import org.reactivestreams.Publisher;
import reactor.core.publisher.*;
import reactor.netty.Connection;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;
import reactor.util.annotation.Nullable;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.rrmq.spi.decoder.AmqpResponseDecoder.MessageType.REPLY_SUCCESS;

public class AmqpReactorNettyClient implements Client {

    private final AtomicReference<Connection> connection = new AtomicReference<>();

    private final AtomicReference<ByteBufAllocator> byteBufAllocator = new AtomicReference<>();

    private final EmitterProcessor<AmqpRequest> requestProcessor = EmitterProcessor.create(false);

    private final FluxSink<AmqpRequest> requests = this.requestProcessor.sink();

    private final AtomicReference<Integer> processId = new AtomicReference<>();

    private final AtomicReference<Integer> secretKey = new AtomicReference<>();

    private final Queue<MonoSink<Flux<AmqpResponse>>> responseReceivers = Queues.<MonoSink<Flux<AmqpResponse>>>unbounded().get();

    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    private final BiConsumer<AmqpResponse, SynchronousSink<AmqpResponse>> handleCloseOkResponse = (response, synchronousSink) -> {
        if (response instanceof CloseOk) {
            synchronousSink.error(new CloseAmqpConnectionException());
        } else {
            synchronousSink.next(response);
        }
    };

    public static Mono<AmqpReactorNettyClient> connect(String host, int port) {
        return connect(ConnectionProvider.newConnection(), host, port, null);
    }

    public static Mono<AmqpReactorNettyClient> connect(ConnectionProvider connectionProvider,
                                                       String host,
                                                       int port,
                                                       @Nullable Duration connectTimeout) {
//        Assert.requireNonNull(connectionProvider, "connectionProvider must not be null");
//        Assert.requireNonNull(host, "host must not be null");

        TcpClient tcpClient = TcpClient.create(connectionProvider)
                .host(host).port(port);

        if (connectTimeout != null) {
            tcpClient = tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(connectTimeout.toMillis()));
        }

        Mono<? extends Connection> connection = tcpClient.connect();

        return connection.map(AmqpReactorNettyClient::new);
    }

    private AmqpReactorNettyClient(Connection connection) {
        //Assert.requireNonNull(connection, "Connection must not be null");

        connection.addHandler(new EnsureSubscribersCompleteChannelHandler(this.requestProcessor, this.responseReceivers));

        ByteBufAllocator alloc = connection.outbound().alloc();
        AmqpResponseReaderDecoder envelopeDecoder = new AmqpResponseReaderDecoder(alloc);
        this.byteBufAllocator.set(alloc);

        Mono<Void> receive = connection.inbound().receive()
                .retain()
                .concatMap(envelopeDecoder)
                .map(AmqpResponseDecoder::decode)
                .doOnNext(message -> System.out.println("Response: " + message))
                .handle(getAmqpResponseSynchronousSinkBiConsumer())
                .windowWhile(not(OpenOk.class::isInstance))
                .doOnNext(fluxOfMessages -> {
                    MonoSink<Flux<AmqpResponse>> receiver = this.responseReceivers.poll();
                    if (receiver != null) {
                        receiver.success(fluxOfMessages);
                    }
                })
                .doOnComplete(() -> {
                    MonoSink<Flux<AmqpResponse>> receiver = this.responseReceivers.poll();
                    if (receiver != null) {
                        receiver.success(Flux.empty());
                    }
                })
                .then();

        Mono<Void> request = this.requestProcessor
                .doOnNext(message -> System.out.println("Request: " + message))
                .concatMap(message -> connection.outbound().send(message.encode(connection.outbound().alloc())))
                .then();

        connection.onDispose()
                .doFinally(s -> {
                    System.out.println(s);
                    envelopeDecoder.dispose();
                })
                .subscribe();

        Flux.merge(receive, request)
                .onErrorResume(throwable -> close())
                .subscribe();

        this.connection.set(connection);
    }

    private BiConsumer<AmqpResponse, SynchronousSink<AmqpResponse>> getAmqpResponseSynchronousSinkBiConsumer() {
        return (response, synchronousSink) -> synchronousSink.next(response);
    }

    @Override
    public Mono<Void> close() {
        return Mono.defer(() -> {
            Connection connection = this.connection.getAndSet(null);

            if (connection == null) {
                return Mono.empty();
            }

            return Flux.just(
                    CloseAmqpMethod.builder()
                            .setReplyCode(REPLY_SUCCESS.getDiscriminator())
                            .setReplyText("OK")
                            .build()
            )
            .doOnNext(message -> System.out.println("Response: " + message))
            .concatMap(message -> connection.outbound().send(message.encode(connection.outbound().alloc())))
            .then()
            .doOnSuccess(v -> connection.dispose())
            .then(connection.onDispose())
            .doOnSuccess(v -> this.isClosed.set(true));
        });
    }

    @Override
    public Flux<AmqpResponse> exchange(Publisher<AmqpRequest> requests) {
        return Mono
                .<Flux<AmqpResponse>>create(sink -> {
                    if (this.isClosed.get()) {
                        sink.error(new IllegalStateException("Cannot exchange messages because the connection is closed"));
                    }

                    final AtomicInteger once = new AtomicInteger();


                    Flux.from(requests)
                            .subscribe(message -> {
                                if (once.get() == 0 && once.compareAndSet(0, 1)) {
                                    synchronized (this) {
                                        this.responseReceivers.add(sink);
                                        this.requests.next(message);
                                    }
                                    return;
                                }
                                this.requests.next(message);
                            }, this.requests::error);

                })
                .flatMapMany(Function.identity());
    }

    @Override
    public ByteBufAllocator getByteBufAllocator() {
        return byteBufAllocator.get();
    }

    @Override
    public Optional<Integer> getProcessId() {
        return Optional.ofNullable(this.processId.get());
    }

    @Override
    public Optional<Integer> getSecretKey() {
        return Optional.ofNullable(this.secretKey.get());
    }

    @Override
    public boolean isConnected() {
        if (this.isClosed.get()) {
            return false;
        }

        Channel channel = this.connection.get().channel();
        return channel.isOpen();
    }


    private static final class EnsureSubscribersCompleteChannelHandler extends ChannelDuplexHandler {

        private final EmitterProcessor<AmqpRequest> requestProcessor;

        private final Queue<MonoSink<Flux<AmqpResponse>>> responseReceivers;

        private EnsureSubscribersCompleteChannelHandler(EmitterProcessor<AmqpRequest> requestProcessor,
                                                        Queue<MonoSink<Flux<AmqpResponse>>> responseReceivers) {
            this.requestProcessor = requestProcessor;
            this.responseReceivers = responseReceivers;
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            super.channelUnregistered(ctx);

            this.requestProcessor.onComplete();

            for (MonoSink<Flux<AmqpResponse>> responseReceiver = this.responseReceivers.poll(); responseReceiver != null; responseReceiver = this.responseReceivers.poll()) {
                responseReceiver.success(Flux.empty());
            }
        }
    }


    @SuppressWarnings("unchecked")
    private static <T extends AmqpResponse> BiConsumer<AmqpResponse, SynchronousSink<AmqpResponse>> handleBackendMessage(
            Class<T> type, BiConsumer<T, SynchronousSink<AmqpResponse>> consumer) {
        return (message, sink) -> {
            if (type.isInstance(message)) {
                consumer.accept((T) message, sink);
            } else {
                sink.next(message);
            }
        };
    }

    public static <T> Predicate<T> not(Predicate<T> t) {

        return t.negate();
    }

}
