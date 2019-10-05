package io.rrmq.spi.method;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.utils.AmqpBuilder;
import io.rrmq.spi.utils.AmqpFrameUtils;
import org.reactivestreams.Publisher;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpWriteUtils.writeShort;

public abstract class BaseFrame implements AmqpResponse, AmqpRequest {

    private final short type;

    private final short channel;

    public BaseFrame(short type, short channel) {
        this.type = type;
        this.channel = channel;
    }

    public BaseFrame(AmqpBuilder<?,?> builder) {
        this.type = builder.getType();
        this.channel = builder.getChannel();
    }

    @Override
    public short getChannel() {
        return channel;
    }

    @Override
    public short getType() {
        return type;
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) {
        writeShort(getProtocolClassId(), out, counter);
        writeShort(getProtocolMethodId(), out, counter);
        writeMethodValues(out, counter);
    }

    @Override
    public Publisher<ByteBuf> encode(ByteBufAllocator byteBufAllocator) {
        return AmqpFrameUtils.encode(this, byteBufAllocator);
    }

    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {};

    @Override
    public String toString() {
        return "BaseFrame{" +
                "type=" + type +
                ", channel=" + channel +
                '}';
    }
}
