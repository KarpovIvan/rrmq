package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.RecoverAsync;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpWriteUtils.writeBits;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.RECOVER_ASYNC;

public class RecoverAsyncAmqpMethod extends BaseFrame implements RecoverAsync {

    private final boolean requeue;

    private RecoverAsyncAmqpMethod(short type, short channel, boolean requeue) {
        super(type, channel);
        this.requeue = requeue;
    }

    private RecoverAsyncAmqpMethod(short type, short channel, ByteBuf in) {
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
        return RECOVER_ASYNC.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeBits(out, counter, this.requeue);
    }

    @Override
    public String toString() {
        return "RecoverAsyncAmqpMethod{" +
                "requeue=" + requeue +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new RecoverAsyncAmqpMethod(type, channel, in);
    }

    public static AmqpResponse of(short type, short channel, boolean requeue) {
        return new RecoverAsyncAmqpMethod(type, channel, requeue);
    }

}
