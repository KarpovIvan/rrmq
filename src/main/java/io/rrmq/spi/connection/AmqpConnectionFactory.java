package io.rrmq.spi.connection;

import io.rrmq.spi.AmqpReactorNettyClient;
import io.rrmq.spi.Client;
import io.rrmq.spi.StartupMessageFlow;
import io.rrmq.spi.exception.AmqpExceptionFactory;
import reactor.core.publisher.Mono;

public class AmqpConnectionFactory implements ConnectionFactory {

    private final Mono<? extends Client> clientFactory;

    private AmqpConnectionConfiguration configuration;

    public AmqpConnectionFactory(Mono<? extends Client> clientFactory) {
        this.clientFactory = clientFactory;
    }

    public AmqpConnectionFactory(AmqpConnectionConfiguration configuration) {
        this.clientFactory = Mono.defer(() -> AmqpReactorNettyClient.connect(configuration.getHost(), configuration.getPort()).cast(Client.class));
    }

    @Override
    public Mono<AmqpConnection> create() {
        return this.clientFactory
                .delayUntil(client -> StartupMessageFlow.exchange(client))
                .flatMap(client -> Mono.fromSupplier(() -> new AmqpConnection(client)));
    }
}
