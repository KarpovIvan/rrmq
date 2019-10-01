package io.rrmq.spi.connection;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.Client;
import io.rrmq.spi.flow.ChannelMessageFlow;
import reactor.core.publisher.Mono;

public class AmqpConnection implements Connection {

    private final Client client;

    public AmqpConnection(Client client) {
        this.client = client;
    }

    @Override
    public Mono<AmqpResponse> createChannel() {
        return ChannelMessageFlow.exchange(client);
    }

    @Override
    public Mono<Void> close() {
        return null;
    }

}
