package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;

import java.util.Map;

public interface QueueBind extends AmqpResponse {

    int getTicket();
    String getQueue();
    String getExchange();
    String getRoutingKey();
    boolean getNowait();
    Map<String,Object> getArguments();

}
