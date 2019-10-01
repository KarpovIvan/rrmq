package io.rrmq.spi.method.channel.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.channel.ChannelFlow;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpWriteUtils.writeBit;
import static io.rrmq.spi.method.ProtocolClassType.CHANEL;
import static io.rrmq.spi.method.channel.ChannelMethodType.FLOW;

public class ChannelFlowAmqpMethod extends BaseFrame implements ChannelFlow {

    private final boolean active;

    private ChannelFlowAmqpMethod(short type, short channel, ByteBuf in) {
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
        return FLOW.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeBit(active, out, counter);
    }

    public static ChannelFlow of(short type, short channel, ByteBuf in) {
        return new ChannelFlowAmqpMethod(type, channel, in);
    }

    @Override
    public String toString() {
        return "ChannelFlowAmqpMethod{" +
                "active=" + active +
                "} " + super.toString();
    }
}
