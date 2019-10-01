package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;

public interface Tune extends AmqpResponse {

    int getChannelMax();
    int getFrameMax();
    int getHeartbeat();

}
