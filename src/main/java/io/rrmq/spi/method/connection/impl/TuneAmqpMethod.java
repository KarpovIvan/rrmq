package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.Tune;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLong;
import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLong;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShort;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.TUNE;

public class TuneAmqpMethod extends BaseFrame implements Tune {

    private int channelMax;
    private int frameMax;
    private int heartbeat;

    private TuneAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.channelMax = readShort(in);
        this.frameMax = readLong(in);
        this.heartbeat = readShort(in);
    }

    @Override
    public int getChannelMax() {
        return channelMax;
    }

    @Override
    public int getFrameMax() {
        return frameMax;
    }

    @Override
    public int getHeartbeat() {
        return heartbeat;
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return TUNE.getDiscriminator();
    }

    @Override
    public void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.channelMax, out, counter);
        writeLong(this.frameMax, out, counter);
        writeShort((short) this.heartbeat, out, counter);
    }

    @Override
    public String toString() {
        return "TuneAmqpMethod{" +
                "channelMax=" + channelMax +
                ", frameMax=" + frameMax +
                ", heartbeat=" + heartbeat +
                "} " + super.toString();
    }

    public static Tune of(short type, short channel, ByteBuf in) {
        return new TuneAmqpMethod(type, channel, in);
    }
}
