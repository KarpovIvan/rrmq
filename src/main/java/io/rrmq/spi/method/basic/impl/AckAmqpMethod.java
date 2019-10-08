package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Ack;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLonglong;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.ACK;

public class AckAmqpMethod extends BaseFrame implements Ack {

    private final long deliveryTag;
    private final boolean multiple;

    private AckAmqpMethod(AckAmqpBuilder<?> builder) {
        super(builder);
        this.deliveryTag = builder.deliveryTag;
        this.multiple = builder.multiple;
    }

    private AckAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.deliveryTag = readLonglong(in);
        this.multiple = in.readBoolean();
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
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLonglong(this.deliveryTag, out, counter);
        writeBits(out, counter, this.multiple);
}

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return ACK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "AckAmqpMethod{" +
                "deliveryTag=" + deliveryTag +
                ", multiple=" + multiple +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new AckAmqpMethod(type, channel, in);
    }

    public static class AckAmqpBuilder<T extends AckAmqpBuilder<T>> extends AmqpBuilder<T, AckAmqpMethod> {

        private long deliveryTag;
        private boolean multiple;

        public T setDeliveryTag(long deliveryTag) {
            this.deliveryTag = deliveryTag;
            return self();
        }

        public T setMultiple(boolean multiple) {
            this.multiple = multiple;
            return self();
        }

        @Override
        public AckAmqpMethod build() {
            return new AckAmqpMethod(self());
        }
    }
}
