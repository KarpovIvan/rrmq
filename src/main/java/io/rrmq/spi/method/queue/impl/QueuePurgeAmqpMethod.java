package io.rrmq.spi.method.queue.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueuePurge;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.PURGE;

public class QueuePurgeAmqpMethod extends BaseFrame implements QueuePurge {

    private final int ticket;
    private final String queue;
    private final boolean nowait;

    private QueuePurgeAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.queue = readShortstr(in);
        this.nowait = in.readBoolean();
    }

    private QueuePurgeAmqpMethod(QueuePurgeBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.queue = builder.queue;
        this.nowait = builder.nowait;
    }

    @Override
    public int getTicket() {
        return this.ticket;
    }

    @Override
    public String getQueue() {
        return this.queue;
    }

    @Override
    public boolean getNowait() {
        return this.nowait;
    }

    @Override
    public short getProtocolClassId() {
        return QUEUE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return PURGE.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.queue, out, counter);
        writeBit(this.nowait, out, counter);
    }

    @Override
    public String toString() {
        return "QueuePurgeAmqpMethod{" +
                "ticket=" + ticket +
                ", queue='" + queue + '\'' +
                ", nowait=" + nowait +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QueuePurgeAmqpMethod(type, channel, in);
    }

    public static class QueuePurgeBuilder<T extends QueuePurgeBuilder<T>> extends AmqpBuilder<T, QueuePurgeAmqpMethod> {

        private int ticket;
        private String queue;
        private boolean nowait;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setQueue(String queue) {
            this.queue = queue;
            return self();
        }

        public T setNowait(boolean nowait) {
            this.nowait = nowait;
            return self();
        }

        @Override
        public QueuePurgeAmqpMethod build() {
            return new QueuePurgeAmqpMethod(self());
        }
    }
}
