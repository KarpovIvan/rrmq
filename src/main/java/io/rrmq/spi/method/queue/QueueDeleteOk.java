package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;

public interface QueueDeleteOk extends AmqpResponse {

    int getMessageCount();

}
