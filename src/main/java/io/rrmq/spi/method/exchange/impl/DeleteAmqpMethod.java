package io.rrmq.spi.method.exchange.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.exchange.Delete;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.EXCHANGE;
import static io.rrmq.spi.method.exchange.ExchangeMethodType.DELETE;

public class DeleteAmqpMethod extends BaseFrame implements Delete {

    private final int ticket;
    private final String exchange;
    private final boolean ifUnused;
    private final boolean nowait;

    private DeleteAmqpMethod(DeleteAmqpBuilder<?> builder) {
        super(builder);
        this.ticket = builder.ticket;
        this.exchange = builder.exchange;
        this.ifUnused = builder.ifUnused;
        this.nowait = builder.nowait;
    }

    private DeleteAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.ticket = readShort(in);
        this.exchange = readShortstr(in);
        this.ifUnused = in.readBoolean();
        this.nowait = in.readBoolean();
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
    public boolean isIfUnused() {
        return ifUnused;
    }

    @Override
    public boolean isNowait() {
        return nowait;
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.ticket, out, counter);
        writeShortstr(this.exchange, out, counter);
        writeBits(out, counter, this.ifUnused, this.nowait);
    }

    @Override
    public short getProtocolClassId() {
        return EXCHANGE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DELETE.getDiscriminator();
    }

    @Override
    public String toString() {
        return "DeleteAmqpMethod{" +
                "ticket=" + ticket +
                ", exchange='" + exchange + '\'' +
                ", ifUnused=" + ifUnused +
                ", nowait=" + nowait +
                "} " + super.toString();
    }

    public static DeleteAmqpBuilder<?> builder() {
        return new DeleteAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new DeleteAmqpMethod(type, channel, in);
    }

    public static class DeleteAmqpBuilder<T extends DeleteAmqpBuilder<T>> extends AmqpBuilder<T, DeleteAmqpMethod> {

        private int ticket;
        private String exchange;
        private boolean ifUnused;
        private boolean nowait;

        public T setTicket(int ticket) {
            this.ticket = ticket;
            return self();
        }

        public T setExchange(String exchange) {
            this.exchange = exchange;
            return self();
        }

        public T setIfUnused(boolean ifUnused) {
            this.ifUnused = ifUnused;
            return self();
        }

        public T setNowait(boolean nowait) {
            this.nowait = nowait;
            return self();
        }

        @Override
        public DeleteAmqpMethod build() {
            return new DeleteAmqpMethod(self());
        }
    }

}
