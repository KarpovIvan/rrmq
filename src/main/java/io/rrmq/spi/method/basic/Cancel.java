package io.rrmq.spi.method.basic;

public interface Cancel {
    String getConsumerTag();
    boolean isNowait();
}
