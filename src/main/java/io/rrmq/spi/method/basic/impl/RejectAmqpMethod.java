package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Reject;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLonglong;
import static io.rrmq.spi.method.AmqpWriteUtils.writeBit;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLonglong;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.REJECT;

public class RejectAmqpMethod extends BaseFrame implements Reject {

    private final long deliveryTag;
    private final boolean requeue;

    private RejectAmqpMethod(RejectAmqpBuilder<?> builder) {
        super(builder);
        this.deliveryTag = builder.deliveryTag;
        this.requeue = builder.requeue;
    }
    private RejectAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.deliveryTag = readLonglong(in);
        this.requeue = in.readBoolean();
    }

    @Override
    public long getDeliveryTag() {
        return deliveryTag;
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
        return REJECT.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLonglong(this.deliveryTag, out, counter);
        writeBit(this.requeue, out, counter);
    }

    @Override
    public String toString() {
        return "RejectAmqpMethod{" +
                "deliveryTag=" + deliveryTag +
                ", requeue=" + requeue +
                "} " + super.toString();
    }

    public static RejectAmqpBuilder<?> builder() {
        return new RejectAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new RejectAmqpMethod(type, channel, in);
    }

    public static class RejectAmqpBuilder<T extends RejectAmqpBuilder<T>> extends AmqpBuilder<T, RejectAmqpMethod> {

        private long deliveryTag;
        private boolean requeue;

        public T setDeliveryTag(long deliveryTag) {
            this.deliveryTag = deliveryTag;
            return self();
        }

        public T setRequeue(boolean requeue) {
            this.requeue = requeue;
            return self();
        }

        @Override
        public RejectAmqpMethod build() {
            return new RejectAmqpMethod(self());
        }
    }

}
