package io.rrmq.spi.method.exchange;

import java.util.Map;

public interface Unbind {
    int getTicket();
    String getDestination();
    String getSource();
    String getRoutingKey();
    boolean isNowait();
    Map<String,Object> getArguments();
}
