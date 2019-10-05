package io.rrmq.spi.method.basic;

public interface Get {
    int getTicket();
    String getQueue();
    boolean isNoAck();
}
