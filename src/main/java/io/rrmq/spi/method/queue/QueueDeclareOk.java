package io.rrmq.spi.method.queue;

public interface QueueDeclareOk {

    String getQueue();
    int getMessageCount();
    int getConsumerCount();

}
