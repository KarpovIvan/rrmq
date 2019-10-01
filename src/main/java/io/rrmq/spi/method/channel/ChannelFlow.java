package io.rrmq.spi.method.channel;

import io.rrmq.spi.AmqpResponse;

public interface ChannelFlow extends AmqpResponse {

    boolean getActive();

}
