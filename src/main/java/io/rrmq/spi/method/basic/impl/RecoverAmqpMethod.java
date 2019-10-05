package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.ProtocolClassType;
import io.rrmq.spi.method.basic.BasicMethodType;
import io.rrmq.spi.method.basic.Recover;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpWriteUtils.writeBit;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.RECOVER;

public class RecoverAmqpMethod extends BaseFrame implements Recover {

    private final boolean requeue;

    private RecoverAmqpMethod(short type, short channel, boolean requeue) {
        super(type, channel);
        this.requeue = requeue;
    }

    private RecoverAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, in.readBoolean());
    }

    @Override
    public boolean isRequeue() {
        return requeue;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return RECOVER.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeBit(this.requeue, out, counter);
    }

    @Override
    public String toString() {
        return "RecoverAmqpMethod{" +
                "requeue=" + requeue +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new RecoverAmqpMethod(type, channel, in);
    }

    public static AmqpResponse of(short type, short channel, boolean requeue) {
        return new RecoverAmqpMethod(type, channel, requeue);
    }

}
