package io.rrmq.spi;

import io.rrmq.spi.connection.AmqpConnection;
import io.rrmq.spi.connection.AmqpConnectionConfiguration;
import io.rrmq.spi.connection.AmqpConnectionFactory;
import io.rrmq.spi.decoder.AmqpResponseDecoder;
import io.rrmq.spi.flow.StartupMessageFlow;
import io.rrmq.spi.header.BasicProperties;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Deliver;
import io.rrmq.spi.method.basic.impl.ConsumeAmqpMethod;
import io.rrmq.spi.method.basic.impl.PublishAmqpMethod;
import io.rrmq.spi.method.channel.Channel;
import io.rrmq.spi.method.connection.OpenOk;
import io.rrmq.spi.method.connection.Start;
import io.rrmq.spi.method.connection.Tune;
import io.rrmq.spi.method.exchange.impl.ExchangeDeclareAmqpMethod;
import io.rrmq.spi.method.queue.impl.QueueBindAmqpMethod;
import io.rrmq.spi.method.queue.impl.QueueDeclareAmqpMethod;
import io.rrmq.spi.method.сonfirm.impl.SelectAmqpMethod;
import org.junit.Test;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.rrmq.spi.decoder.AmqpResponseDecoder.MessageType.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class AmqpConnectionTest {

    private static final Duration TIME_OUT = Duration.ofSeconds(30);
    private static final String LOCALHOST = "localhost";
    private static final int PORT = 5672;

    private AmqpConnectionFactory amqpConnectionFactory = new AmqpConnectionFactory(new AmqpConnectionConfiguration(LOCALHOST, PORT));

    @Test
    public void testConnection() {
        StepVerifier.withVirtualTime(() -> AmqpReactorNettyClient.connect(LOCALHOST, PORT)
                .flatMapMany(StartupMessageFlow::exchange))
                .assertNext(response -> assertThat(response, instanceOf(Start.class)))
                .assertNext(response -> assertThat(response, instanceOf(Tune.class)))
                .assertNext(response -> assertThat(response, instanceOf(OpenOk.class)))
                .expectError()
                .verify(TIME_OUT);
    }

    @Test
    public void createChannel() throws InterruptedException {
        byte[] solods = new String("Sacha public hello!").getBytes();

        String queue_1 = "queue_4";
        String exchange_1 = "exchange_4";
        amqpConnectionFactory.create()
                .flatMap(AmqpConnection::createChannel)
                .delayUntil(channel -> channel.declareQueue(
                        QueueDeclareAmqpMethod.builder()
                                .setQueue(queue_1)
                                .setDurable(true)
                                .setChannel((short) 1)
                                .build())
                )
                .delayUntil(channel -> channel.declareExchange(
                        ExchangeDeclareAmqpMethod.builder()
                                .setExchange(exchange_1)
                                .setType("fanout")
                                .setDurable(true)
                                .setChannel((short) 1)
                                .build())
                        .then())
                .delayUntil(channel -> channel.declareBinding(
                        QueueBindAmqpMethod.builder()
                                .setQueue(queue_1)
                                .setExchange(exchange_1)
                                .setRoutingKey("")
                                .setChannel((short) 1)
                                .build()))
//                .delayUntil(channel -> {
//                    List<AmqpRequest> list = new ArrayList<>();
//                    for (int i = 0; i < 1000000; i++) {
//                        list.add(SelectAmqpMethod.of(FRAME_METHOD.getDiscriminator(), (short) 1, false));
//                        list.add(PublishAmqpMethod.builder()
//                                .setChannel((short) 1)
//                                .setExchange(exchange_1)
//                                .setRoutingKey("")
//                                .build());
//                        list.add(BasicProperties.builder()
//                                .setChannel((short) 1)
//                                .setType((short) FRAME_HEADER.getDiscriminator())
//                                .setBodySize(solods.length)
//                                .build());
//                        list.add(BodyFrame.of(FRAME_BODY.getDiscriminator(), (short) 1, solods));
//                    }
//                    return channel.sendMany(Flux.fromIterable(list));
//                })
                .flatMapMany(channel -> channel.consume(
                        ConsumeAmqpMethod.builder()
                                .setChannel((short) 1)
                                .setQueue(queue_1)
                                .setConsumerTag("")
                                .build())
                )
                .subscribe();

        Thread.sleep(100000L);
    }

}
