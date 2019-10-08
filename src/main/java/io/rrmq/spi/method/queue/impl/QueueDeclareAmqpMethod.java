package io.rrmq.spi.method.queue.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueueDeclare;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.DECLARE;

public class QueueDeclareAmqpMethod extends BaseFrame implements QueueDeclare {

    private final int ticket;
    private final String queue;
    private final boolean passive;
    private final boolean durable;
    private final boolean exclusive;
    private final boolean autoDelete;
    private final boolean nowait;
    private final Map<String,Object> arguments;

    private QueueDeclareAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.queue = readShortstr(in);
        this.passive = in.readBoolean();
        this.durable = in.readBoolean();
        this.exclusive = in.readBoolean();
        this.autoDelete = in.readBoolean();
        this.nowait = in.readBoolean();
        this.arguments = readTable(in);
    }

    private QueueDeclareAmqpMethod(QueueDeclareBuilder<?> builder) {
        super(builder.getType(), builder.getChannel());
        this.ticket = builder.ticket;
        this.queue = builder.queue;
        this.passive = builder.passive;
        this.durable = builder.durable;
        this.exclusive = builder.exclusive;
        this.autoDelete = builder.autoDelete;
        this.nowait = builder.nowait;
        this.arguments = builder.arguments;
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
    public boolean getPassive() {
        return this.passive;
    }

    @Override
    public boolean getDurable() {
        return this.durable;
    }

    @Override
    public boolean getExclusive() {
        return this.exclusive;
    }

    @Override
    public boolean getAutoDelete() {
        return this.autoDelete;
    }

    @Override
    public boolean getNowait() {
        return this.nowait;
    }

    @Override
    public Map<String, Object> getArguments() {
        return this.arguments;
    }

    @Override
    public short getProtocolClassId() {
        return QUEUE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DECLARE.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.queue, out, counter);
        writeBits(out, counter, this.passive, this.durable, this.exclusive, this.autoDelete, this.nowait);
        writeTable(this.arguments, out, counter);
    }

    @Override
    public String toString() {
        return "QueueDeclareAmqpMethod{" +
                "ticket=" + ticket +
                ", queue='" + queue + '\'' +
                ", passive=" + passive +
                ", durable=" + durable +
                ", exclusive=" + exclusive +
                ", autoDelete=" + autoDelete +
                ", nowait=" + nowait +
                ", arguments=" + arguments +
                "} " + super.toString();
    }

    public static QueueDeclareBuilder<?> builder() {
        return new QueueDeclareBuilder<>();
    }

    public static class QueueDeclareBuilder<T extends QueueDeclareBuilder<T>> extends AmqpBuilder<T, QueueDeclareAmqpMethod> {

        private int ticket;
        private String queue;
        private boolean passive;
        private boolean durable;
        private boolean exclusive;
        private boolean autoDelete;
        private boolean nowait;
        private Map<String,Object> arguments;
        public QueueDeclareBuilder<T> setTicket(int ticket) {
            this.ticket = ticket;
            return this;
        }

        public QueueDeclareBuilder<T> setQueue(String queue) {
            this.queue = queue;
            return this;
        }

        public QueueDeclareBuilder<T> setPassive(boolean passive) {
            this.passive = passive;
            return this;
        }

        public QueueDeclareBuilder<T> setDurable(boolean durable) {
            this.durable = durable;
            return this;
        }

        public QueueDeclareBuilder<T> setExclusive(boolean exclusive) {
            this.exclusive = exclusive;
            return this;
        }

        public QueueDeclareBuilder<T> setAutoDelete(boolean autoDelete) {
            this.autoDelete = autoDelete;
            return this;
        }

        public QueueDeclareBuilder<T> setNowait(boolean nowait) {
            this.nowait = nowait;
            return this;
        }

        public QueueDeclareBuilder<T> setArguments(Map<String, Object> arguments) {
            this.arguments = arguments;
            return this;
        }

        @Override
        public QueueDeclareAmqpMethod build() {
            return new QueueDeclareAmqpMethod(self());
        }

    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QueueDeclareAmqpMethod(type, channel, in);
    }

}
