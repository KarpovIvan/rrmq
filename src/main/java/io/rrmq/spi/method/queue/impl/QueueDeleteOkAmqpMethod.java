package io.rrmq.spi.method.queue.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueueDeleteOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLong;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLong;
import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.DELETE_OK;

public class QueueDeleteOkAmqpMethod extends BaseFrame implements QueueDeleteOk {

    private final int messageCount;

    private QueueDeleteOkAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readLong(in));
    }

    private QueueDeleteOkAmqpMethod(short type, short channel, int messageCount) {
        super(type, channel);
        this.messageCount = messageCount;
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public short getProtocolClassId() {
        return QUEUE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DELETE_OK.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLong(this.messageCount, out, counter);
    }

    @Override
    public String toString() {
        return "QueueDeleteOkAmqpMethod{" +
                "messageCount=" + messageCount +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QueueDeleteOkAmqpMethod(type, channel, in);
    }

    public static AmqpResponse of(short type, short channel, int messageCount) {
        return new QueueDeleteOkAmqpMethod(type, channel, messageCount);
    }
}
