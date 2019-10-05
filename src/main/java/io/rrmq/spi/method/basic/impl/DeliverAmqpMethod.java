package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Deliver;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLonglong;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.DELIVERY;

public class DeliverAmqpMethod extends BaseFrame implements Deliver {

    private final String consumerTag;
    private final long deliveryTag;
    private final boolean redelivered;
    private final String exchange;
    private final String routingKey;

    private DeliverAmqpMethod(DeliverAmqpBuilder<?> builder) {
        super(builder);
        this.consumerTag = builder.consumerTag;
        this.deliveryTag = builder.deliveryTag;
        this.redelivered = builder.redelivered;
        this.exchange = builder.exchange;
        this.routingKey = builder.routingKey;
    }

    private DeliverAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.consumerTag = readShortstr(in);
        this.deliveryTag = readLonglong(in);
        this.redelivered = in.readBoolean();
        this.exchange = readShortstr(in);
        this.routingKey = readShortstr(in);
    }

    @Override
    public String getConsumerTag() {
        return consumerTag;
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
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.consumerTag, out, counter);
        writeLonglong(this.deliveryTag, out, counter);
        writeBit(this.redelivered, out, counter);
        writeShortstr(this.exchange, out, counter);
        writeShortstr(this.routingKey, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DELIVERY.getDiscriminator();
    }

    @Override
    public String toString() {
        return "DeliverAmqpMethod{" +
                "consumerTag='" + consumerTag + '\'' +
                ", deliveryTag=" + deliveryTag +
                ", redelivered=" + redelivered +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                "} " + super.toString();
    }

    public static DeliverAmqpBuilder<?> builder() {
        return new DeliverAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new DeliverAmqpMethod(type, channel, in);
    }

    public static class DeliverAmqpBuilder<T extends DeliverAmqpBuilder<T>> extends AmqpBuilder<T, DeliverAmqpMethod> {

        private String consumerTag;
        private long deliveryTag;
        private boolean redelivered;
        private String exchange;
        private String routingKey;

        public T setConsumerTag(String consumerTag) {
            this.consumerTag = consumerTag;
            return self();
        }

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

        @Override
        public DeliverAmqpMethod build() {
            return new DeliverAmqpMethod(self());
        }
    }

}
