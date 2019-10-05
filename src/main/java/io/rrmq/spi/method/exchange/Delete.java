package io.rrmq.spi.method.exchange;

public interface Delete {
    int getTicket();
    String getExchange();
    boolean isIfUnused();
    boolean isNowait();
}
