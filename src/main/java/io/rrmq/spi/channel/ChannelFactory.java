package io.rrmq.spi.channel;

import org.reactivestreams.Publisher;

public interface ChannelFactory {

    Publisher<? extends Channel> create();

}
