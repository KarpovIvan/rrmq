package io.rrmq.spi;

import io.rrmq.spi.connection.AmqpConnectionConfiguration;
import io.rrmq.spi.connection.AmqpConnectionFactory;
import io.rrmq.spi.method.connection.OpenOk;
import io.rrmq.spi.method.connection.Start;
import io.rrmq.spi.method.connection.Tune;
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
                .assertNext(response ->  assertThat(response, instanceOf(Start.class)))
                .assertNext(response ->  assertThat(response, instanceOf(Tune.class)))
                .assertNext(response ->  assertThat(response, instanceOf(OpenOk.class)))
                .expectError()
                .verify(TIME_OUT);
    }

    @Test
    public void createChannel() throws InterruptedException {
        amqpConnectionFactory.create()
                .flatMap(amqpConnection -> amqpConnection.createChannel())
                .subscribe();

        Thread.sleep(100000L);
    }

}