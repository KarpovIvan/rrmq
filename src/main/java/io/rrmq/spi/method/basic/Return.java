package io.rrmq.spi.method.basic;

public interface Return {
    int getReplyCode();
    String getReplyText();
    String getExchange();
    String getRoutingKey();
}
