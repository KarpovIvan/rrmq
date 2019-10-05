package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Return;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShort;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.RETURN;

public class ReturnAmqpMethod extends BaseFrame implements Return {

    private final int replyCode;
    private final String replyText;
    private final String exchange;
    private final String routingKey;

    private ReturnAmqpMethod(ReturnAmqpBuilder<?> builder) {
        super(builder);
        this.replyCode = builder.replyCode;
        this.replyText = builder.replyText;
        this.exchange = builder.exchange;
        this.routingKey = builder.routingKey;
    }
    private ReturnAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.replyCode = readShort(in);
        this.replyText = readShortstr(in);
        this.exchange = readShortstr(in);
        this.routingKey = readShortstr(in);
    }

    @Override
    public int getReplyCode() {
        return replyCode;
    }

    @Override
    public String getReplyText() {
        return replyText;
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
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return RETURN.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.replyCode, out, counter);
        writeShortstr(this.replyText, out, counter);
        writeShortstr(this.exchange, out, counter);
        writeShortstr(this.routingKey, out, counter);
    }

    @Override
    public String toString() {
        return "ReturnAmqpMethod{" +
                "replyCode=" + replyCode +
                ", replyText='" + replyText + '\'' +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                "} " + super.toString();
    }

    public static ReturnAmqpBuilder<?> builder() {
        return new ReturnAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new ReturnAmqpMethod(type, channel, in);
    }

    public static class ReturnAmqpBuilder<T extends ReturnAmqpBuilder<T>> extends AmqpBuilder<T, ReturnAmqpMethod> {

        private int replyCode;
        private String replyText;
        private String exchange;
        private String routingKey;

        public T setReplyCode(int replyCode) {
            this.replyCode = replyCode;
            return self();
        }

        public T setReplyText(String replyText) {
            this.replyText = replyText;
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

        @Override
        public ReturnAmqpMethod build() {
            return new ReturnAmqpMethod(self());
        }
    }

}
