package io.rrmq.spi.method.exchange.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.exchange.Declare;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.EXCHANGE;
import static io.rrmq.spi.method.exchange.ExchangeMethodType.DECLARE;

public class DeclareAmqpMethod extends BaseFrame implements Declare {

    private final int ticket;
    private final String exchange;
    private final String type;
    private final boolean passive;
    private final boolean durable;
    private final boolean autoDelete;
    private final boolean internal;
    private final boolean nowait;
    private final Map<String,Object> arguments;

    private DeclareAmqpMethod(DeclareAmqpBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.exchange = builder.exchange;
        this.type = builder.type;
        this.passive = builder.passive;
        this.durable = builder.durable;
        this.autoDelete = builder.autoDelete;
        this.internal = builder.internal;
        this.nowait = builder.nowait;
        this.arguments = builder.arguments;
    }

    private DeclareAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.exchange = readShortstr(in);
        this.type = readShortstr(in);
        this.passive = in.readBoolean();
        this.durable = in.readBoolean();
        this.autoDelete = in.readBoolean();
        this.internal = in.readBoolean();
        this.nowait = in.readBoolean();
        this.arguments = readTable(in);
    }

    @Override
    public int getTicket() {
        return ticket;
    }

    @Override
    public String getExchange() {
        return exchange;
    }

    @Override
    public String getExchangeType() {
        return type;
    }

    @Override
    public boolean isPassive() {
        return passive;
    }

    @Override
    public boolean isDurable() {
        return durable;
    }

    @Override
    public boolean isAutoDelete() {
        return autoDelete;
    }

    @Override
    public boolean isInternal() {
        return internal;
    }

    @Override
    public boolean isNowait() {
        return nowait;
    }

    @Override
    public Map<String, Object> getArguments() {
        return arguments;
    }

    @Override
    public short getProtocolClassId() {
        return EXCHANGE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DECLARE.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.exchange, out, counter);
        writeShortstr(this.type, out, counter);
        writeBit(this.passive, out, counter);
        writeBit(this.durable, out, counter);
        writeBit(this.autoDelete, out, counter);
        writeBit(this.internal, out, counter);
        writeBit(this.nowait, out, counter);
        writeTable(this.arguments, out, counter);
    }

    @Override
    public String toString() {
        return "DeclareAmqpMethod{" +
                "ticket=" + ticket +
                ", exchange='" + exchange + '\'' +
                ", type='" + type + '\'' +
                ", passive=" + passive +
                ", durable=" + durable +
                ", autoDelete=" + autoDelete +
                ", internal=" + internal +
                ", nowait=" + nowait +
                ", arguments=" + arguments +
                "} " + super.toString();
    }
    public static DeclareAmqpBuilder<?> builder() {
        return new DeclareAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new DeclareAmqpMethod(type, channel, in);
    }

    public static class DeclareAmqpBuilder<T extends DeclareAmqpBuilder<T>> extends AmqpBuilder<T, DeclareAmqpMethod> {

        private int ticket;
        private String exchange;
        private String type;
        private boolean passive;
        private boolean durable;
        private boolean autoDelete;
        private boolean internal;
        private boolean nowait;
        private Map<String,Object> arguments;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setExchange(String exchange) {
            this.exchange = exchange;
            return self();
        }

        public T setType(String type) {
            this.type = type;
            return self();
        }

        public T setPassive(boolean passive) {
            this.passive = passive;
            return self();
        }

        public T setDurable(boolean durable) {
            this.durable = durable;
            return self();
        }

        public T setAutoDelete(boolean autoDelete) {
            this.autoDelete = autoDelete;
            return self();
        }

        public T setInternal(boolean internal) {
            this.internal = internal;
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
        public DeclareAmqpMethod build() {
            return new DeclareAmqpMethod(self());
        }
    }

}
