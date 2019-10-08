package io.rrmq.spi;

import io.rrmq.spi.connection.AmqpConnection;
import io.rrmq.spi.connection.AmqpConnectionConfiguration;
import io.rrmq.spi.connection.AmqpConnectionFactory;
import io.rrmq.spi.flow.StartupMessageFlow;
import io.rrmq.spi.method.connection.OpenOk;
import io.rrmq.spi.method.connection.Start;
import io.rrmq.spi.method.connection.Tune;
import io.rrmq.spi.method.exchange.ExchangeBind;
import io.rrmq.spi.method.exchange.impl.ExchangeBindAmqpMethod;
import io.rrmq.spi.method.exchange.impl.ExchangeDeclareAmqpMethod;
import io.rrmq.spi.method.queue.impl.QueueBindAmqpMethod;
import io.rrmq.spi.method.queue.impl.QueueDeclareAmqpMethod;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

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
        amqpConnectionFactory.create()
                .flatMap(AmqpConnection::createChannel)
                .delayUntil(channel -> channel.declareQueue(
                        QueueDeclareAmqpMethod.builder()
                                .setQueue("queue_1")
                                .setDurable(true)
                                .setChannel((short) 1)
                                .build())
                )
                .delayUntil(channel -> channel.declareExchange(
                        ExchangeDeclareAmqpMethod.builder()
                                .setExchange("exchange_1")
                                .setType("fanout")
                                .setDurable(true)
                                .setChannel((short) 1)
                                .build())
                        .then())
                .delayUntil(channel -> channel.declareBinding(
                        QueueBindAmqpMethod.builder()
                                .setQueue("queue_1")
                                .setExchange("exchange_1")
                                .setRoutingKey("")
                                .setChannel((short) 1)
                                .build()

                ))
                .subscribe();

        Thread.sleep(100000L);
    }

}
