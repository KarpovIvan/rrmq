package io.rrmq.spi.method.exchange;

import io.rrmq.spi.AmqpRequest;

import java.util.Map;

public interface ExchangeBind extends AmqpRequest {
    int getTicket();
    String getDestination();
    String getSource();
    String getRoutingKey();
    boolean isNowait();
    Map<String,Object> getArguments();

}
