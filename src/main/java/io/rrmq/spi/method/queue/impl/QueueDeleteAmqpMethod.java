package io.rrmq.spi.method.queue.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueueDelete;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.DELETE;

public class QueueDeleteAmqpMethod extends BaseFrame implements QueueDelete {

    private final int ticket;
    private final String queue;
    private final boolean ifUnused;
    private final boolean ifEmpty;
    private final boolean nowait;

    private QueueDeleteAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.queue = readShortstr(in);
        this.ifUnused = in.readBoolean();
        this.ifEmpty = in.readBoolean();
        this.nowait = in.readBoolean();
    }

    private QueueDeleteAmqpMethod(QueueDeleteBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.queue = builder.queue;
        this.ifUnused = builder.ifUnused;
        this.ifEmpty = builder.ifEmpty;
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
    public boolean getIfUnused() {
        return this.ifUnused;
    }

    @Override
    public boolean getIfEmpty() {
        return this.ifEmpty;
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
        return DELETE.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.queue, out, counter);
        writeBits(out, counter, this.ifUnused, this.ifEmpty, this.nowait);
    }

    @Override
    public String toString() {
        return "QueueDeleteAmqpMethod{" +
                "ticket=" + ticket +
                ", queue='" + queue + '\'' +
                ", ifUnused=" + ifUnused +
                ", ifEmpty=" + ifEmpty +
                ", nowait=" + nowait +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QueueDeleteAmqpMethod(type, channel, in);
    }

    public static class QueueDeleteBuilder<T extends QueueDeleteBuilder<T>> extends AmqpBuilder<T, QueueDeleteAmqpMethod> {


        private int ticket;
        private String queue;
        private boolean ifUnused;
        private boolean ifEmpty;
        private boolean nowait;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setQueue(String queue) {
            this.queue = queue;
            return self();
        }

        public T setIfUnused(boolean ifUnused) {
            this.ifUnused = ifUnused;
            return self();
        }

        public T setIfEmpty(boolean ifEmpty) {
            this.ifEmpty = ifEmpty;
            return self();
        }

        public T setNowait(boolean nowait) {
            this.nowait = nowait;
            return self();
        }

        @Override
        public QueueDeleteAmqpMethod build() {
            return new QueueDeleteAmqpMethod(self());
        }
    }
}
