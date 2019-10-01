package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.rrmq.spi.AmqpRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class ProtocolAmqpMethod implements AmqpRequest {
    private static final ByteBuf AMQP_HEADER = Unpooled.copiedBuffer("AMQP", US_ASCII).asReadOnly();

    @Override
    public short getProtocolClassId() {
        return 0;
    }

    @Override
    public short getProtocolMethodId() {
        return 0;
    }

    @Override
    public short getChannel() {
        return 0;
    }

    @Override
    public short getType() {
        return 0;
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) { }

    @Override
    public Publisher<ByteBuf> encode(ByteBufAllocator byteBufAllocator) {
        return Mono.defer(() -> {
            ByteBuf out = byteBufAllocator.ioBuffer();
            out.writeBytes(AMQP_HEADER);
            out.writeByte(0);
            out.writeByte(0);
            out.writeByte(9);
            out.writeByte(1);
            return Mono.just(out);
        });
    }

}
