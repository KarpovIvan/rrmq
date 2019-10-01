package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.TuneOk;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLong;
import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpWriteUtils.writeInt;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShort;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.TUNE_OK;

public class TuneOkAmqpMethod extends BaseFrame implements TuneOk {

    private int channelMax;
    private int frameMax;
    private int heartbeat;

    public static TuneOkBuilder<?> builder() {
        return new TuneOkBuilder<>();
    }

    private TuneOkAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.channelMax = readShort(in);
        this.frameMax = readLong(in);
        this.heartbeat = readShort(in);
    }

    private TuneOkAmqpMethod(TuneOkBuilder<?> builder) {
        super(builder.getType(), builder.getChannel());
        this.channelMax = builder.channelMax;
        this.frameMax = builder.frameMax;
        this.heartbeat = builder.heartMax;
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
        return TUNE_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "TuneOkAmqpMethod{" +
                "channelMax=" + channelMax +
                ", frameMax=" + frameMax +
                ", heartbeat=" + heartbeat +
                "} " + super.toString();
    }

    @Override
    public void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.channelMax, out, counter);
        writeInt(this.frameMax, out, counter);
        writeShort((short) this.heartbeat, out, counter);
    }

    public static class TuneOkBuilder<T extends TuneOkBuilder<T>> extends AmqpBuilder<T, TuneOkAmqpMethod> {

        private int channelMax;
        private int frameMax;
        private int heartMax;

        public T setChannelMax(int channelMax) {
            this.channelMax = channelMax;
            return self();
        }

        public T setFrameMax(int frameMax) {
            this.frameMax = frameMax;
            return self();
        }

        public T setHeartMax(int heartMax) {
            this.heartMax = heartMax;
            return self();
        }

        @Override
        public TuneOkAmqpMethod build() {
            return new TuneOkAmqpMethod(self());
        }
    }

    public static TuneOk of(short type, short channel, ByteBuf in) {
        return new TuneOkAmqpMethod(type, channel, in);
    }
}
