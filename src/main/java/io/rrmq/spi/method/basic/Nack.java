package io.rrmq.spi.method.basic;

public interface Nack {
    long getDeliveryTag();
    boolean isMultiple();
    boolean isRequeue();
}
