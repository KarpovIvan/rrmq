package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.GetOk;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.GET_OK;

public class GetOkAmqpMethod extends BaseFrame implements GetOk {

    private final long deliveryTag;
    private final boolean redelivered;
    private final String exchange;
    private final String routingKey;
    private final int messageCount;

    private GetOkAmqpMethod(GetOkAmqpBuilder<?> builder) {
        super(builder);
        this.deliveryTag = builder.deliveryTag;
        this.redelivered = builder.redelivered;
        this.exchange = builder.exchange;
        this.routingKey = builder.routingKey;
        this.messageCount = builder.messageCount;
    }
    private GetOkAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.deliveryTag = readLonglong(in);
        this.redelivered = in.readBoolean();
        this.exchange = readShortstr(in);
        this.routingKey = readShortstr(in);
        this.messageCount = readLong(in);
    }

    @Override
    public long getDeliveryTag() {
        return deliveryTag;
    }

    public boolean isRedelivered() {
        return redelivered;
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
    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return GET_OK.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLonglong(this.deliveryTag, out, counter);
        writeBit(this.redelivered, out, counter);
        writeShortstr(this.exchange, out, counter);
        writeShortstr(this.routingKey, out, counter);
        writeLong(this.messageCount, out, counter);
    }

    @Override
    public String toString() {
        return "GetOkAmqpMethod{" +
                "deliveryTag=" + deliveryTag +
                ", redelivered=" + redelivered +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", messageCount=" + messageCount +
                "} " + super.toString();
    }

    public static GetOkAmqpBuilder<?> builder() {
        return new GetOkAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new GetOkAmqpMethod(type, channel, in);
    }

    public static class GetOkAmqpBuilder<T extends GetOkAmqpBuilder<T>> extends AmqpBuilder<T, GetOkAmqpMethod> {

        private long deliveryTag;
        private boolean redelivered;
        private String exchange;
        private String routingKey;
        private int messageCount;

        public T setDeliveryTag(long deliveryTag) {
            this.deliveryTag = deliveryTag;
            return self();
        }

        public T setRedelivered(boolean redelivered) {
            this.redelivered = redelivered;
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

        public T setMessageCount(int messageCount) {
            this.messageCount = messageCount;
            return self();
        }

        @Override
        public GetOkAmqpMethod build() {
            return new GetOkAmqpMethod(self());
        }
    }

}
