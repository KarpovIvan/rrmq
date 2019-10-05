package io.rrmq.spi.method.access;

public interface Request {
    String getRealm();
    boolean isExclusive();
    boolean isPassive();
    boolean isActive();
    boolean isWrite();
    boolean isRead();
}
