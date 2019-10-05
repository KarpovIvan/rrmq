package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;

public interface QueueDelete extends AmqpResponse {

    int getTicket();
    String getQueue();
    boolean getIfUnused();
    boolean getIfEmpty();
    boolean getNowait();

}
