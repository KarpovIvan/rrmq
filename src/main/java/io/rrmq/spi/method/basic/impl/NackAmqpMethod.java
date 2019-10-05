package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Nack;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLonglong;
import static io.rrmq.spi.method.AmqpWriteUtils.writeBit;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLonglong;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.NACK;

public class NackAmqpMethod extends BaseFrame implements Nack {

    private final long deliveryTag;
    private final boolean multiple;
    private final boolean requeue;

    private NackAmqpMethod(NackAmqpBuilder<?> builder) {
        super(builder);
        this.deliveryTag = builder.deliveryTag;
        this.multiple = builder.multiple;
        this.requeue = builder.requeue;
    }

    private NackAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.deliveryTag = readLonglong(in);
        this.multiple = in.readBoolean();
        this.requeue = in.readBoolean();
    }

    @Override
    public long getDeliveryTag() {
        return deliveryTag;
    }

    @Override
    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public boolean isRequeue() {
        return requeue;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return NACK.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLonglong(this.deliveryTag, out, counter);
        writeBit(this.multiple, out, counter);
        writeBit(this.requeue, out, counter);
    }

    @Override
    public String toString() {
        return "NackAmqpMethod{" +
                "deliveryTag=" + deliveryTag +
                ", multiple=" + multiple +
                ", requeue=" + requeue +
                "} " + super.toString();
    }

    public static NackAmqpBuilder<?> builder() {
        return new NackAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new NackAmqpMethod(type, channel, in);
    }

    public static class NackAmqpBuilder<T extends NackAmqpBuilder<T>> extends AmqpBuilder<T, NackAmqpMethod> {

        private long deliveryTag;
        private boolean multiple;
        private boolean requeue;

        public T setDeliveryTag(long deliveryTag) {
            this.deliveryTag = deliveryTag;
            return self();
        }

        public T setMultiple(boolean multiple) {
            this.multiple = multiple;
            return self();
        }

        public T setRequeue(boolean requeue) {
            this.requeue = requeue;
            return self();
        }

        @Override
        public NackAmqpMethod build() {
            return new NackAmqpMethod(self());
        }
    }

}
