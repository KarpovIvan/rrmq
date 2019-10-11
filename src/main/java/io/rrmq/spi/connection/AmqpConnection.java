package io.rrmq.spi.connection;

import io.rrmq.spi.Client;
import io.rrmq.spi.flow.ChannelMessageFlow;
import io.rrmq.spi.method.channel.Channel;
import io.rrmq.spi.method.channel.impl.BaseChannel;
import reactor.core.publisher.Mono;

public class AmqpConnection implements Connection {

    private final Client client;

    public AmqpConnection(Client client) {
        this.client = client;
    }

    @Override
    public Mono<Channel> createChannel() {
        return ChannelMessageFlow.exchange(client)
                .then(Mono.just(new BaseChannel(client)));
    }

    @Override
    public Mono<Void> close() {
        return null;
    }

}
