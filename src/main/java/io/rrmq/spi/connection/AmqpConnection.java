package io.rrmq.spi.connection;

import io.rrmq.spi.Client;
import io.rrmq.spi.channel.AmqpChannelFactory;
import io.rrmq.spi.channel.ChannelFactory;
import io.rrmq.spi.channel.Channel;
import reactor.core.publisher.Mono;

public class AmqpConnection implements Connection {

    private final Client client;

    private ChannelFactory channelFactory;

    public AmqpConnection(Client client, ChannelFactory channelFactory) {
        this.client = client;
        this.channelFactory = channelFactory;
    }

    @Override
    public Mono<Channel> createChannel() {
        return Mono.from(channelFactory.create());
    }

    @Override
    public Mono<Void> close() {
        return null;
    }

}
