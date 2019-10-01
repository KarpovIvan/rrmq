package io.rrmq.spi.method.channel;

import io.rrmq.spi.AmqpResponse;

public interface ChannelOpen extends AmqpResponse {

    String getOutOfBand();

}
