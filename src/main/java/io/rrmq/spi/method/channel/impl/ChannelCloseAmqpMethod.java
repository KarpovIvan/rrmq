package io.rrmq.spi.method.channel.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.channel.ChannelClose;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShort;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.CHANEL;
import static io.rrmq.spi.method.channel.ChannelMethodType.CLOSE;

public class ChannelCloseAmqpMethod extends BaseFrame implements ChannelClose {

    private final int replyCode;
    private final String replyText;
    private final int classId;
    private final int methodId;

    private ChannelCloseAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.replyCode = readShort(in);
        this.replyText = readShortstr(in);
        this.classId = readShort(in);
        this.methodId = readShort(in);
    }

    @Override
    public int getReplyCode() {
        return replyCode;
    }

    @Override
    public String getReplyText() {
        return replyText;
    }

    @Override
    public int getClassId() {
        return classId;
    }

    @Override
    public int getMethodId() {
        return methodId;
    }

    @Override
    public short getProtocolClassId() {
        return CHANEL.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return CLOSE.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.replyCode, out, counter);
        writeShortstr(this.replyText, out, counter);
        writeShort((short) this.classId, out, counter);
        writeShort((short) this.methodId, out, counter);
    }

    public static ChannelClose of(short type, short channel, ByteBuf in) {
        return new ChannelCloseAmqpMethod(type, channel, in);
    }

    @Override
    public String toString() {
        return "ChannelCloseAmqpMethod{" +
                "replyCode=" + replyCode +
                ", replyText='" + replyText + '\'' +
                ", classId=" + classId +
                ", methodId=" + methodId +
                "} " + super.toString();
    }
}
