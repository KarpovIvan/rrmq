package io.rrmq.spi.method.basic;

public interface Qos {
    int getPrefetchSize();
    int getPrefetchCount();
    boolean isGlobal();
}
