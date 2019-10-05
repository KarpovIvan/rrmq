package io.rrmq.spi.method.basic;

public interface Reject {
    long getDeliveryTag();
    boolean isRequeue();
}
