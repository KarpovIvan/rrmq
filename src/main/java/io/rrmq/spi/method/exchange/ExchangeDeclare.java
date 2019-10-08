package io.rrmq.spi.method.exchange;

import io.rrmq.spi.AmqpRequest;

import java.util.Map;

public interface ExchangeDeclare extends AmqpRequest {
    int getTicket();
    String getExchange();
    String getExchangeType();
    boolean isPassive();
    boolean isDurable();
    boolean isAutoDelete();
    boolean isInternal();
    boolean isNowait();
    Map<String,Object> getArguments();
}
