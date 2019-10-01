package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.Unblocked;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.UN_BLOCKED;

public class UnblockedAmqpMethod extends BaseFrame implements Unblocked {

    private UnblockedAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) { }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return UN_BLOCKED.getDiscriminator();
    }

    public static Unblocked of(short type, short channel) {
        return new UnblockedAmqpMethod(type, channel);
    }

    @Override
    public String toString() {
        return "UnblockedAmqpMethod{} " + super.toString();
    }
}
