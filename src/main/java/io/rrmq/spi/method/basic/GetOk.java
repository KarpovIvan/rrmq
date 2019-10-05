package io.rrmq.spi.method.basic;

public interface GetOk {
    long getDeliveryTag();
    boolean isRedelivered();
    String getExchange();
    String getRoutingKey();
    int getMessageCount();
}
