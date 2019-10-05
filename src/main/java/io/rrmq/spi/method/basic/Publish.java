package io.rrmq.spi.method.basic;

public interface Publish {
    int getTicket();
    String getExchange();
    String getRoutingKey();
    boolean isMandatory();
    boolean isImmediate();
}
