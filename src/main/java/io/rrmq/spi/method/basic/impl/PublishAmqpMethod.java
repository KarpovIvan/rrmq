package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Publish;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.PUBLISH;

public class PublishAmqpMethod extends BaseFrame implements Publish {

    private final int ticket;
    private final String exchange;
    private final String routingKey;
    private final boolean mandatory;
    private final boolean immediate;

    private PublishAmqpMethod(PublishAmqpBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.exchange = builder.exchange;
        this.routingKey = builder.routingKey;
        this.mandatory = builder.mandatory;
        this.immediate = builder.immediate;
    }

    private PublishAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.exchange = readShortstr(in);
        this.routingKey = readShortstr(in);
        this.mandatory = in.readBoolean();
        this.immediate = in.readBoolean();
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
    public String getRoutingKey() {
        return routingKey;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public boolean isImmediate() {
        return immediate;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return PUBLISH.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.exchange, out, counter);
        writeShortstr(this.routingKey, out, counter);
        writeBit(this.mandatory, out, counter);
        writeBit(this.immediate, out, counter);
    }

    @Override
    public String toString() {
        return "PublishAmqpMethod{" +
                "ticket=" + ticket +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", mandatory=" + mandatory +
                ", immediate=" + immediate +
                "} " + super.toString();
    }

    public static PublishAmqpBuilder<?> builder() {
        return new PublishAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new PublishAmqpMethod(type, channel, in);
    }

    public static class PublishAmqpBuilder<T extends PublishAmqpBuilder<T>> extends AmqpBuilder<T, PublishAmqpMethod> {

        private int ticket;
        private String exchange;
        private String routingKey;
        private boolean mandatory;
        private boolean immediate;

        public T setTicket(int ticket) {
            this.ticket = ticket;
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

        public T setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
            return self();
        }

        public T setImmediate(boolean immediate) {
            this.immediate = immediate;
            return self();
        }

        @Override
        public PublishAmqpMethod build() {
            return new PublishAmqpMethod(self());
        }
    }

}
