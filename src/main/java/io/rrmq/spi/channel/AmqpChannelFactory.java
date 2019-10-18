package io.rrmq.spi.channel;

import io.rrmq.spi.Client;
import io.rrmq.spi.flow.ChannelMessageFlow;
import org.reactivestreams.Publisher;

public class AmqpChannelFactory implements ChannelFactory {

    private Client client;

    private ChannelAllocator channelAllocator;

    public AmqpChannelFactory(Client client) {
        this.client = client;
        this.channelAllocator = new ChannelAllocator(1, 64500);
    }

    @Override
    public Publisher<? extends Channel> create() {
        return ChannelMessageFlow.exchange(client, channelId());
    }

    private int channelId() {
        synchronized (this) {
            return channelAllocator.allocate();
        }
    }

}
