package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;

import java.util.Map;

public interface QueueUnbind extends AmqpResponse {

    int getTicket();
    String getQueue();
    String getExchange();
    String getRoutingKey();
    Map<String,Object> getArguments();

}
