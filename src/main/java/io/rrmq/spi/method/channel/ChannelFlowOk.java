package io.rrmq.spi.method.channel;

import io.rrmq.spi.AmqpResponse;

public interface ChannelFlowOk extends AmqpResponse {

    boolean getActive();

}
