package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.CloseOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.CLOSE_OK;

public class CloseOkAmqpMethod extends BaseFrame implements CloseOk {

    private CloseOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {}

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return CLOSE_OK.getDiscriminator();
    }

    public static CloseOk of(short type, short channel) {
        return new CloseOkAmqpMethod(type, channel);
    }

    @Override
    public String toString() {
        return "CloseOkAmqpMethod{} " + super.toString();
    }
}
