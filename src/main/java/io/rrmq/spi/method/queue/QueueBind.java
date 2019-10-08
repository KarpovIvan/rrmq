package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.connection.FluxFinish;

import java.util.Map;

public interface QueueBind extends AmqpResponse, AmqpRequest, FluxFinish {

    int getTicket();
    String getQueue();
    String getExchange();
    String getRoutingKey();
    boolean getNowait();
    Map<String,Object> getArguments();

}
