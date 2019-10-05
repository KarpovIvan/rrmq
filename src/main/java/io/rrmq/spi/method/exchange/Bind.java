package io.rrmq.spi.method.exchange;

import java.util.Map;

public interface Bind {
    int getTicket();
    String getDestination();
    String getSource();
    String getRoutingKey();
    boolean isNowait();
    Map<String,Object> getArguments();

}
