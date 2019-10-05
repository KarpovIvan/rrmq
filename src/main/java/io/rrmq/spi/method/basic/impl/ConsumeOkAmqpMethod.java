package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.ConsumeOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.CONSUME_OK;

public class ConsumeOkAmqpMethod extends BaseFrame implements ConsumeOk {

    private final String consumerTag;

    private ConsumeOkAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readShortstr(in));
    }

    private ConsumeOkAmqpMethod(short type, short channel, String consumerTag) {
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
        return CONSUME_OK.getDiscriminator();
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.consumerTag, out, counter);
    }

    @Override
    public String toString() {
        return "ConsumeOkAmqpMethod{" +
                "consumerTag='" + consumerTag + '\'' +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new ConsumeOkAmqpMethod(type, channel, in);
    }

    public static ConsumeOkAmqpMethod of(short type, short channel, String consumerTag) {
        return new ConsumeOkAmqpMethod(type, channel, consumerTag);
    }

}
