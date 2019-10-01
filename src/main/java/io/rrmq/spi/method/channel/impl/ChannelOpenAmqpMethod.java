package io.rrmq.spi.method.channel.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.channel.ChannelMethodType;
import io.rrmq.spi.method.channel.ChannelOpen;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.CHANEL;

public class ChannelOpenAmqpMethod extends BaseFrame implements ChannelOpen {

    private final String outOfBand;

    private ChannelOpenAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readShortstr(in));
    }

    private ChannelOpenAmqpMethod(short type, short channel, String outOfBand) {
        super(type, channel);
        this.outOfBand = outOfBand;
    }

    @Override
    public String getOutOfBand() {
        return outOfBand;
    }

    @Override
    public short getProtocolClassId() {
        return CHANEL.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return ChannelMethodType.OPEN.getDiscriminator();
    }

    @Override
    public void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(outOfBand, out, counter);
    }

    public static ChannelOpen of(short type, short channel, ByteBuf in) {
        return new ChannelOpenAmqpMethod(type, channel, in);
    }

    public static ChannelOpenAmqpMethod of(short type, short channel, String outOfBand) {
        return new ChannelOpenAmqpMethod(type, channel, outOfBand);
    }

    @Override
    public String toString() {
        return "ChannelOpenAmqpMethod{" +
                "outOfBand='" + outOfBand + '\'' +
                "} " + super.toString();
    }
}
