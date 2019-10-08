package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.AmqpResponse;

import java.util.Map;

public interface QueueDeclare extends AmqpResponse, AmqpRequest {

    int getTicket();
    String getQueue();
    boolean getPassive();
    boolean getDurable();
    boolean getExclusive();
    boolean getAutoDelete();
    boolean getNowait();
    Map<String,Object> getArguments();

}
