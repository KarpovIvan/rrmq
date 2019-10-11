package io.rrmq.spi.method.basic;

import io.rrmq.spi.AmqpRequest;

import java.util.Map;

public interface Consume extends AmqpRequest {
    int getTicket();
    String getQueue();
    String getConsumerTag();
    boolean isNoLocal();
    boolean isNoAck();
    boolean isExclusive();
    boolean isNowait();
    Map<String,Object> getArguments();
}
