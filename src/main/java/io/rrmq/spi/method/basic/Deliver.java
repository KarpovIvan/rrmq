package io.rrmq.spi.method.basic;

public interface Deliver {
    String getConsumerTag();
    long getDeliveryTag();
    boolean isRedelivered();
    String getExchange();
    String getRoutingKey();
}
