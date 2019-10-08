package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Get;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.GET;

public class GetAmqpMethod extends BaseFrame implements Get {

    private final int ticket;
    private final String queue;
    private final boolean noAck;

    private GetAmqpMethod(GetAmqpBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.queue = builder.queue;
        this.noAck = builder.noAck;
    }

    private GetAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.queue = readShortstr(in);
        this.noAck = in.readBoolean();
    }

    @Override
    public int getTicket() {
        return ticket;
    }

    @Override
    public String getQueue() {
        return queue;
    }

    public boolean isNoAck() {
        return noAck;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return GET.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.queue, out, counter);
        writeBits(out, counter, this.noAck);
    }

    @Override
    public String toString() {
        return "GetAmqpMethod{" +
                "ticket=" + ticket +
                ", queue='" + queue + '\'' +
                ", noAck=" + noAck +
                "} " + super.toString();
    }

    public static GetAmqpBuilder<?> builder() {
        return new GetAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new GetAmqpMethod(type, channel, in);
    }

    public static class GetAmqpBuilder<T extends GetAmqpBuilder<T>> extends AmqpBuilder<T, GetAmqpMethod> {

        private int ticket;
        private String queue;
        private boolean noAck;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setQueue(String queue) {
            this.queue = queue;
            return self();
        }

        public T setNoAck(boolean noAck) {
            this.noAck = noAck;
            return self();
        }

        @Override
        public GetAmqpMethod build() {
            return new GetAmqpMethod(self());
        }
    }

}
