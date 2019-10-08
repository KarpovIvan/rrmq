package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Qos;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLong;
import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.QOS;

public class QosAmqpMethod extends BaseFrame implements Qos {

    private final int prefetchSize;
    private final int prefetchCount;
    private final boolean global;

    private QosAmqpMethod(QosAmqpBuilder<?> builder) {
        super(builder);
        this.prefetchSize = builder.prefetchSize;
        this.prefetchCount = builder.prefetchCount;
        this.global = builder.global;
    }
    private QosAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.prefetchSize = readLong(in);
        this.prefetchCount = readShort(in);
        this.global = in.readBoolean();
    }

    @Override
    public int getPrefetchSize() {
        return prefetchSize;
    }

    @Override
    public int getPrefetchCount() {
        return prefetchCount;
    }

    @Override
    public boolean isGlobal() {
        return global;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return QOS.getDiscriminator();
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLong(this.prefetchSize, out, counter);
        writeShort((short) this.prefetchCount, out, counter);
        writeBits(out, counter, this.global);
    }

    @Override
    public String toString() {
        return "QosAmqpMethod{" +
                "prefetchSize=" + prefetchSize +
                ", prefetchCount=" + prefetchCount +
                ", global=" + global +
                "} " + super.toString();
    }

    public static QosAmqpBuilder<?> builder() {
        return new QosAmqpBuilder<>();
    }
    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QosAmqpMethod(type, channel, in);
    }

    public static class QosAmqpBuilder<T extends QosAmqpBuilder<T>> extends AmqpBuilder<T, QosAmqpMethod> {

        private int prefetchSize;
        private int prefetchCount;
        private boolean global;

        public T setPrefetchSize(int prefetchSize) {
            this.prefetchSize = prefetchSize;
            return self();
        }

        public T setPrefetchCount(int prefetchCount) {
            this.prefetchCount = prefetchCount;
            return self();
        }

        public T setGlobal(boolean global) {
            this.global = global;
            return self();
        }

        @Override
        public QosAmqpMethod build() {
            return new QosAmqpMethod(self());
        }
    }

}
