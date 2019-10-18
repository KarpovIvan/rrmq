package io.rrmq.spi.channel;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.pool.*;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class ChannelPool implements ChannelFactory {

    private Pool<Channel> channelPool;

    private final Duration maxAcquireTime;

    public ChannelPool(ChannelPoolConfiguration configuration, ChannelFactory factory) {
        this.channelPool = createConnectionPool(configuration, factory);
        this.maxAcquireTime = configuration.getMaxAcquireTime();
    }

    private InstrumentedPool<Channel> createConnectionPool(ChannelPoolConfiguration configuration, ChannelFactory factory) {
        Duration maxCreateConnectionTime = configuration.getMaxCreateChannelTime();

        int initialSize = configuration.getInitialSize();
        int maxSize = configuration.getMaxSize();


        if (factory instanceof ChannelPool) {
            Loggers.getLogger(ChannelPool.class).warn(String.format("Creating ConnectionPool using another ConnectionPool [%s] as ConnectionFactory", factory));
        }

        // set timeout for create connection
        Mono<Channel> allocator = Mono.from(factory.create());
        if (!maxCreateConnectionTime.isZero()) {
            allocator = allocator.timeout(maxCreateConnectionTime);
        }

        PoolBuilder<Channel, PoolConfig<Channel>> builder = PoolBuilder.from(allocator)
                .destroyHandler(Channel::close)
                .sizeBetween(0, Runtime.getRuntime().availableProcessors());

        if (maxSize == -1 || initialSize > 0) {
            builder.sizeBetween(Math.max(0, initialSize), maxSize == -1 ? Integer.MAX_VALUE : maxSize);
        } else {
            builder.sizeBetween(initialSize, maxSize);
        }

        return builder.fifo();
    }

    @Override
    public Publisher<? extends Channel> create() {
        return Mono.defer(() -> {

            AtomicReference<PooledRef<Channel>> emitted = new AtomicReference<>();

            Mono<PooledChannel> mono = this.channelPool.acquire()
                    .doOnNext(emitted::set)
                    .map(PooledChannel::new)
                    .doOnCancel(() -> {

                        PooledRef<Channel> ref = emitted.get();
                        if (ref != null && emitted.compareAndSet(ref, null)) {
                            ref.release().subscribe();
                        }
                    });

            if (!this.maxAcquireTime.isZero()) {
                mono = mono.timeout(this.maxAcquireTime);
            }
            return mono;
        });
    }

}
