package io.rrmq.spi.method.queue;

import io.rrmq.spi.method.connection.FluxFinish;

public interface QueueDeclareOk extends FluxFinish {

    String getQueue();
    int getMessageCount();
    int getConsumerCount();

}
