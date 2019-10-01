package io.rrmq.spi.method.channel.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.channel.ChannelOpenOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLongstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLongstr;
import static io.rrmq.spi.method.ProtocolClassType.CHANEL;
import static io.rrmq.spi.method.channel.ChannelMethodType.OPEN_OK;

public class ChannelOpenOkAmqpMethod extends BaseFrame implements ChannelOpenOk {

    private final LongString channelId;

    private ChannelOpenOkAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.channelId = readLongstr(in);
    }

    @Override
    public LongString getChannelId() {
        return channelId;
    }

    @Override
    public short getProtocolClassId() {
        return CHANEL.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return OPEN_OK.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLongstr(channelId, out, counter);
    }

    public static ChannelOpenOk of(short type, short channel, ByteBuf in) {
        return new ChannelOpenOkAmqpMethod(type, channel, in);
    }

    @Override
    public String toString() {
        return "ChannelOpenOkAmqpMethod{" +
                "channelId=" + channelId +
                "} " + super.toString();
    }
}
