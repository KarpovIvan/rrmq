package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;

public interface QueueOkDeclare extends AmqpResponse {

    String getQueue();
    int getMessageCount();
    int getConsumerCount();

}
