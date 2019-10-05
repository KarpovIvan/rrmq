package io.rrmq.spi.method.queue.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueueDeclareOk;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLong;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.DECLARE_OK;

public class QueueDeclareOkAmqpMethod extends BaseFrame implements QueueDeclareOk {

    private final String queue;
    private final int messageCount;
    private final int consumerCount;

    private QueueDeclareOkAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.queue = readShortstr(in);
        this.messageCount = readShort(in);
        this.consumerCount = readShort(in);
    }

    private QueueDeclareOkAmqpMethod(QueueDeclareOkBuilder<?> builder) {
        super(builder);
        this.queue = builder.queue;
        this.messageCount = builder.messageCount;
        this.consumerCount = builder.consumerCount;
    }

    @Override
    public short getProtocolClassId() {
        return QUEUE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DECLARE_OK.getDiscriminator();
    }

    @Override
    public String getQueue() {
        return this.queue;
    }

    @Override
    public int getMessageCount() {
        return this.messageCount;
    }

    @Override
    public int getConsumerCount() {
        return this.consumerCount;
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.queue, out, counter);
        writeLong(this.messageCount, out, counter);
        writeLong(this.consumerCount, out, counter);
    }

    @Override
    public String toString() {
        return "QueueDeclareOkAmqpMethod{" +
                "queue='" + queue + '\'' +
                ", messageCount=" + messageCount +
                ", consumerCount=" + consumerCount +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QueueDeclareOkAmqpMethod(type, channel, in);
    }

    public static class QueueDeclareOkBuilder<T extends QueueDeclareOkBuilder<T>> extends AmqpBuilder<T, QueueDeclareOkAmqpMethod> {

        private String queue;
        private int messageCount;
        private int consumerCount;

        public T setQueue(String queue) {
            this.queue = queue;
            return self();
        }

        public T setMessageCount(int messageCount) {
            this.messageCount = messageCount;
            return self();
        }

        public T setConsumerCount(int consumerCount) {
            this.consumerCount = consumerCount;
            return self();
        }

        @Override
        public QueueDeclareOkAmqpMethod build() {
            return new QueueDeclareOkAmqpMethod(self());
        }
    }
}
