package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.Close;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShort;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.CLOSE;

public class CloseAmqpMethod extends BaseFrame implements Close {

    private final int replyCode;
    private final String replyText;
    private final int classId;
    private final int methodId;

    public static CloseBuilder<?> builder() {
        return new CloseBuilder<>();
    }

    public CloseAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.replyCode = readShort(in);
        this.replyText = readShortstr(in);
        this.classId = readShort(in);
        this.methodId = readShort(in);
    }

    public CloseAmqpMethod(CloseBuilder<?> closeBuilder) {
        super(closeBuilder.getType(), closeBuilder.getChannel());
        this.replyCode = closeBuilder.replyCode;
        this.replyText = closeBuilder.replyText;
        this.classId = closeBuilder.classId;
        this.methodId = closeBuilder.methodId;
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShort((short) this.replyCode, out, counter);
        writeShortstr(this.replyText, out, counter);
        writeShort((short) this.classId, out, counter);
        writeShort((short) this.methodId, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return CLOSE.getDiscriminator();
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
    public int getClassId() {
        return classId;
    }

    @Override
    public int getMethodId() {
        return methodId;
    }

    @Override
    public String toString() {
        return "CloseAmqpMethod{" +
                "replyCode=" + replyCode +
                ", replyText='" + replyText + '\'' +
                ", classId=" + classId +
                ", methodId=" + methodId +
                "} " + super.toString();
    }

    public static class CloseBuilder<T extends CloseBuilder<T>> extends AmqpBuilder<T, Close> {

        private int replyCode;
        private String replyText;
        private int classId;
        private int methodId;

        public T setReplyCode(int replyCode) {
            this.replyCode = replyCode;
            return self();
        }

        public T setReplyText(String replyText) {
            this.replyText = replyText;
            return self();
        }

        public T setClassId(int classId) {
            this.classId = classId;
            return self();
        }

        public T setMethodId(int methodId) {
            this.methodId = methodId;
            return self();
        }

        @Override
        public CloseAmqpMethod build() {
            return new CloseAmqpMethod(self());
        }

    }

    public static Close of(short type, short channel, ByteBuf in) {
        return new CloseAmqpMethod(type, channel, in);
    }
}
