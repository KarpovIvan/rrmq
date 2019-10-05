package io.rrmq.spi.method.exchange;

import java.util.Map;

public interface Declare {
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
