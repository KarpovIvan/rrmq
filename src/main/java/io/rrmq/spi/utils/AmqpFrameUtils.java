package io.rrmq.spi.utils;

import com.rabbitmq.client.AMQP;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.rrmq.spi.AmqpRequest;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;


public class AmqpFrameUtils {

    public static Mono<ByteBuf> encode(AmqpRequest request, ByteBufAllocator byteBufAllocator) {
        return Mono.defer(() -> {
            ByteBuf out = byteBufAllocator.ioBuffer();
            out.writeByte(request.getType());
            out.writeShort(request.getChannel());

            AtomicInteger counter = new AtomicInteger();
            ByteBuf payload = byteBufAllocator.ioBuffer();
            request.writeValues(payload, counter);

            out.writeInt(counter.get());
            out.writeBytes(payload);
            payload.release();

            out.writeByte(AMQP.FRAME_END);

            return Mono.just(out);
        });
    }
}
