package io.rrmq.spi.method.exchange.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.exchange.ExchangeBind;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.EXCHANGE;
import static io.rrmq.spi.method.exchange.ExchangeMethodType.BIND;

public class ExchangeBindAmqpMethod extends BaseFrame implements ExchangeBind {

    private final int ticket;
    private final String destination;
    private final String source;
    private final String routingKey;
    private final boolean nowait;
    private final Map<String,Object> arguments;

    private ExchangeBindAmqpMethod(BindAmqpBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.destination = builder.destination;
        this.source = builder.source;
        this.routingKey = builder.routingKey;
        this.nowait = builder.nowait;
        this.arguments = builder.arguments;
    }

    private ExchangeBindAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.destination = readShortstr(in);
        this.source = readShortstr(in);
        this.routingKey = readShortstr(in);
        this.nowait = in.readBoolean();
        this.arguments = readTable(in);
    }

    @Override
    public int getTicket() {
        return ticket;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getRoutingKey() {
        return routingKey;
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
        writeShortstr(this.destination, out, counter);
        writeShortstr(this.source, out, counter);
        writeShortstr(this.routingKey, out, counter);
        writeBits(out, counter, this.nowait);
        writeTable(this.arguments, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return EXCHANGE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return BIND.getDiscriminator();
    }

    @Override
    public String toString() {
        return "BindAmqpMethod{" +
                "ticket=" + ticket +
                ", destination='" + destination + '\'' +
                ", source='" + source + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", nowait=" + nowait +
                ", arguments=" + arguments +
                "} " + super.toString();
    }

    public static BindAmqpBuilder<?> builder() {
        return new BindAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new ExchangeBindAmqpMethod(type, channel, in);
    }

    public static class BindAmqpBuilder<T extends BindAmqpBuilder<T>> extends AmqpBuilder<T, ExchangeBindAmqpMethod> {

        private int ticket;
        private String destination;
        private String source;
        private String routingKey;
        private boolean nowait;
        private Map<String,Object> arguments;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setDestination(String destination) {
            this.destination = destination;
            return self();
        }

        public T setSource(String source) {
            this.source = source;
            return self();
        }

        public T setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
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
        public ExchangeBindAmqpMethod build() {
            return new ExchangeBindAmqpMethod(self());
        }
    }

}
