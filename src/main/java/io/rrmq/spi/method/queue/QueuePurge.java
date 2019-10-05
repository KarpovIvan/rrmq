package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;

public interface QueuePurge extends AmqpResponse {

    int getTicket();
    String getQueue();
    boolean getNowait();

}
