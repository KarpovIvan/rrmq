package io.rrmq.spi.method.сonfirm.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.сonfirm.Select;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpWriteUtils.writeBits;
import static io.rrmq.spi.method.ProtocolClassType.CONFIRM;
import static io.rrmq.spi.method.сonfirm.ConfirmMethodType.SELECT;

public class SelectAmqpMethod extends BaseFrame implements Select {

    private final boolean nowait;

    public SelectAmqpMethod(short type, short channel, boolean nowait) {
        super(type, channel);
        this.nowait = nowait;
    }

    public SelectAmqpMethod(short type, short channel, ByteBuf in) {
       this(type, channel, in.readBoolean());
    }

    @Override
    public boolean isNowait() {
        return nowait;
    }

    @Override
    public short getProtocolClassId() {
        return CONFIRM.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return SELECT.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeBits(out, counter, this.nowait);
    }

    @Override
    public String toString() {
        return "SelectAmqpMethod{" +
                "nowait=" + nowait +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new SelectAmqpMethod(type, channel, in);
    }

    public static SelectAmqpMethod of(short type, short channel,  boolean nowait) {
        return new SelectAmqpMethod(type, channel, nowait);
    }
}
