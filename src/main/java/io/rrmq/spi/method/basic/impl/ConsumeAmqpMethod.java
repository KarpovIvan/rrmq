package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Consume;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.CONSUME;

public class ConsumeAmqpMethod extends BaseFrame implements Consume {

    private final int ticket;
    private final String queue;
    private final String consumerTag;
    private final boolean noLocal;
    private final boolean noAck;
    private final boolean exclusive;
    private final boolean nowait;
    private final Map<String,Object> arguments;

    private ConsumeAmqpMethod(ConsumeAmqpBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.queue = builder.queue;
        this.consumerTag = builder.consumerTag;
        this.noLocal = builder.noLocal;
        this.noAck = builder.noAck;
        this.exclusive = builder.exclusive;
        this.nowait = builder.nowait;
        this.arguments = builder.arguments;
    }

    private ConsumeAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.queue = readShortstr(in);
        this.consumerTag = readShortstr(in);
        this.noLocal = in.readBoolean();
        this.noAck = in.readBoolean();
        this.exclusive = in.readBoolean();
        this.nowait = in.readBoolean();
        this.arguments = readTable(in);
    }

    @Override
    public int getTicket() {
        return ticket;
    }

    @Override
    public String getQueue() {
        return queue;
    }

    @Override
    public String getConsumerTag() {
        return consumerTag;
    }

    public boolean isNoLocal() {
        return noLocal;
    }

    public boolean isNoAck() {
        return noAck;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public boolean isNowait() {
        return nowait;
    }

    @Override
    public Map<String, Object> getArguments() {
        return arguments;
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.queue, out, counter);
        writeShortstr(this.consumerTag, out, counter);
        writeBits(out, counter, this.noLocal, this.noAck, this.exclusive, this.nowait);
        writeTable(this.arguments, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return CONSUME.getDiscriminator();
    }

    @Override
    public String toString() {
        return "ConsumeAmqpMethod{" +
                "ticket=" + ticket +
                ", queue='" + queue + '\'' +
                ", consumerTag='" + consumerTag + '\'' +
                ", noLocal=" + noLocal +
                ", noAck=" + noAck +
                ", exclusive=" + exclusive +
                ", nowait=" + nowait +
                ", arguments=" + arguments +
                "} " + super.toString();
    }

    public static ConsumeAmqpBuilder<?> builder() {
        return new ConsumeAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new ConsumeAmqpMethod(type, channel, in);
    }

    public static class ConsumeAmqpBuilder<T extends ConsumeAmqpBuilder<T>> extends AmqpBuilder<T, ConsumeAmqpMethod> {
        private int ticket;
        private String queue;
        private String consumerTag;
        private boolean noLocal;
        private boolean noAck;
        private boolean exclusive;
        private boolean nowait;
        private Map<String,Object> arguments;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setQueue(String queue) {
            this.queue = queue;
            return self();
        }

        public T setConsumerTag(String consumerTag) {
            this.consumerTag = consumerTag;
            return self();
        }

        public T setNoLocal(boolean noLocal) {
            this.noLocal = noLocal;
            return self();
        }

        public T setNoAck(boolean noAck) {
            this.noAck = noAck;
            return self();
        }

        public T setExclusive(boolean exclusive) {
            this.exclusive = exclusive;
            return self();
        }

        public T setNowait(boolean nowait) {
            this.nowait = nowait;
            return self();
        }

        public T setArguments(Map<String, Object> arguments) {
            this.arguments = arguments;
            return self();
        }

        @Override
        public ConsumeAmqpMethod build() {
            return new ConsumeAmqpMethod(self());
        }
    }

}
