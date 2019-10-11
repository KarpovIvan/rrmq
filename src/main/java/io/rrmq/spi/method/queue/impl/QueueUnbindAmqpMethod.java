package io.rrmq.spi.method.queue.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueueUnbind;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.UNBIND;

public class QueueUnbindAmqpMethod extends BaseFrame implements QueueUnbind {

    private final int ticket;
    private final String queue;
    private final String exchange;
    private final String routingKey;
    private final Map<String,Object> arguments;

    private QueueUnbindAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.queue = readShortstr(in);
        this.exchange = readShortstr(in);
        this.routingKey = readShortstr(in);
        this.arguments = readTable(in);
    }

    private QueueUnbindAmqpMethod(QueueUnbindBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.queue = builder.queue;
        this.exchange = builder.exchange;
        this.routingKey = builder.routingKey;
        this.arguments = builder.arguments;
    }

    @Override
    public short getProtocolClassId() {
        return QUEUE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return UNBIND.getDiscriminator();
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
    public String getExchange() {
        return this.exchange;
    }

    @Override
    public String getRoutingKey() {
        return this.routingKey;
    }

    @Override
    public Map<String, Object> getArguments() {
        return this.arguments;
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.queue, out, counter);
        writeShortstr(this.exchange, out, counter);
        writeShortstr(this.routingKey, out, counter);
        writeTable(this.arguments, out, counter);
    }

    @Override
    public String toString() {
        return "QueueUnbindAmqpMethod{" +
                "ticket=" + ticket +
                ", queue='" + queue + '\'' +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", arguments=" + arguments +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QueueUnbindAmqpMethod(type, channel, in);
    }

    public static class QueueUnbindBuilder<T extends QueueUnbindBuilder<T>> extends AmqpBuilder<T, QueueUnbindAmqpMethod> {

        private int ticket;
        private String queue;
        private String exchange;
        private String routingKey;
        private Map<String,Object> arguments;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setQueue(String queue) {
            this.queue = queue;
            return self();
        }

        public T setExchange(String exchange) {
            this.exchange = exchange;
            return self();
        }

        public T setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
            return self();
        }

        public T setArguments(Map<String, Object> arguments) {
            this.arguments = arguments;
            return self();
        }

        @Override
        public QueueUnbindAmqpMethod build() {
            return new QueueUnbindAmqpMethod(self());
        }
    }
}
