package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.CancelOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.CANCEL_OK;

public class CancelOkAmqpMethod extends BaseFrame implements CancelOk {

    private final String consumerTag;

    private CancelOkAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readShortstr(in));
    }

    private CancelOkAmqpMethod(short type, short channel, String consumerTag) {
        super(type, channel);
        this.consumerTag = consumerTag;
    }

    @Override
    public String getConsumerTag() {
        return consumerTag;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return CANCEL_OK.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.consumerTag, out, counter);
    }

    @Override
    public String toString() {
        return "CancelOkAmqpMethod{" +
                "consumerTag='" + consumerTag + '\'' +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new CancelOkAmqpMethod(type, channel, in);
    }

    public static CancelOkAmqpMethod of(short type, short channel, String consumerTag) {
        return new CancelOkAmqpMethod(type, channel, consumerTag);
    }

}
