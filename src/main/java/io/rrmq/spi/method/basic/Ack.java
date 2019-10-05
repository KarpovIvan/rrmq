package io.rrmq.spi.method.basic;

public interface Ack {
    long getDeliveryTag();
    boolean isMultiple();
}
