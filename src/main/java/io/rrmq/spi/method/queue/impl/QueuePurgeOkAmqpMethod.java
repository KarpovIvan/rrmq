package io.rrmq.spi.method.queue.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.ProtocolClassType;
import io.rrmq.spi.method.queue.QueueMethodType;
import io.rrmq.spi.method.queue.QueuePurgeOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLong;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLong;
import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.BIND_OK;

public class QueuePurgeOkAmqpMethod extends BaseFrame implements QueuePurgeOk {

    private final int messageCount;

    private QueuePurgeOkAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readLong(in));
    }

    private QueuePurgeOkAmqpMethod(short type, short channel, int messageCount) {
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
        return BIND_OK.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLong(this.messageCount, out, counter);
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QueuePurgeOkAmqpMethod(type, channel, in);
    }

    public static QueuePurgeOkAmqpMethod of(short type, short channel, int messageCount) {
        return new QueuePurgeOkAmqpMethod(type, channel, messageCount);
    }

    @Override
    public String toString() {
        return "QueuePurgeOkAmqpMethod{" +
                "messageCount=" + messageCount +
                "} " + super.toString();
    }
}
