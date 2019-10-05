package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.Cancel;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeBit;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.CANCEL;

public class CancelAmqpMethod extends BaseFrame implements Cancel {

    private final String consumerTag;
    private final boolean nowait;

    private CancelAmqpMethod(CancelAmqpBuilder<?> builder) {
        super(builder);
        this.consumerTag = builder.consumerTag;
        this.nowait = builder.nowait;
    }
    private CancelAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.consumerTag = readShortstr(in);
        this.nowait = in.readBoolean();
    }

    @Override
    public String getConsumerTag() {
        return consumerTag;
    }

    @Override
    public boolean isNowait() {
        return nowait;
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.consumerTag, out, counter);
        writeBit(this.nowait, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return CANCEL.getDiscriminator();
    }

    @Override
    public String toString() {
        return "CancelAmqpMethod{" +
                "consumerTag='" + consumerTag + '\'' +
                ", nowait=" + nowait +
                "} " + super.toString();
    }

    public static CancelAmqpBuilder<?> builder() {
        return new CancelAmqpBuilder<>();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new CancelAmqpMethod(type, channel, in);
    }

    public static class CancelAmqpBuilder<T extends CancelAmqpBuilder<T>> extends AmqpBuilder<T, CancelAmqpMethod> {

        private String consumerTag;
        private boolean nowait;

        public T setConsumerTag(String consumerTag) {
            this.consumerTag = consumerTag;
            return self();
        }

        public T setNowait(boolean nowait) {
            this.nowait = nowait;
            return self();
        }

        @Override
        public CancelAmqpMethod build() {
            return new CancelAmqpMethod(self());
        }

    }

}
