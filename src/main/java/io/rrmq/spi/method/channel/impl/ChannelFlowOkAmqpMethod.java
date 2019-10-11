package io.rrmq.spi.method.channel.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.channel.ChannelFlowOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpWriteUtils.writeBits;
import static io.rrmq.spi.method.ProtocolClassType.CHANEL;
import static io.rrmq.spi.method.channel.ChannelMethodType.FLOW_OK;

public class ChannelFlowOkAmqpMethod extends BaseFrame implements ChannelFlowOk {

    private final boolean active;

    private ChannelFlowOkAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.active = in.readBoolean();
    }

    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public short getProtocolClassId() {
        return CHANEL.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return FLOW_OK.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeBits(out, counter, active);
    }

    public static ChannelFlowOk of(short type, short channel, ByteBuf in) {
        return new ChannelFlowOkAmqpMethod(type, channel, in);
    }

    @Override
    public String toString() {
        return "ChannelFlowOkAmqpMethod{" +
                "active=" + active +
                "} " + super.toString();
    }
}
