package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;

public interface QueuePurgeOk extends AmqpResponse {

    int getMessageCount();

}
