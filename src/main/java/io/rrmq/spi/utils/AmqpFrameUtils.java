package io.rrmq.spi.utils;

import com.rabbitmq.client.AMQP;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.BodyFrame;
import io.rrmq.spi.header.BasicProperties;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.decoder.AmqpResponseDecoder.MessageType.*;


public class AmqpFrameUtils {

    public static Mono<ByteBuf> encode(AmqpRequest request, ByteBufAllocator byteBufAllocator) {
        return Mono.defer(() -> {
            if (request.getType() == FRAME_METHOD.getDiscriminator()) {
                return getMethodByteBuf(request, byteBufAllocator);
            } else if (request.getType() == FRAME_HEADER.getDiscriminator()) {
                return getHeaderByteBuf(request, byteBufAllocator);
            } else if (request.getType() == FRAME_BODY.getDiscriminator()) {
                return getBodyByteBuf(request, byteBufAllocator);
            }
            throw new RuntimeException();
        });
    }

    private static Mono<ByteBuf> getMethodByteBuf(AmqpRequest request, ByteBufAllocator byteBufAllocator) {
        ByteBuf out = byteBufAllocator.ioBuffer();
        out.writeByte(request.getType());
        out.writeShort(request.getChannel());

        writePayLoad(request, byteBufAllocator, out);

        out.writeByte(AMQP.FRAME_END);
        return Mono.just(out);
    }

    private static Mono<ByteBuf> getHeaderByteBuf(AmqpRequest request, ByteBufAllocator byteBufAllocator) {
        ByteBuf out = byteBufAllocator.ioBuffer();
        if (request instanceof BasicProperties) {
            BasicProperties basicProperties = (BasicProperties) request;
            out.writeByte(basicProperties.getType());
            out.writeShort(basicProperties.getChannel());

            writePayLoad(request, byteBufAllocator, out);

            out.writeByte(AMQP.FRAME_END);
        }
        return Mono.just(out);
    }

    private static Mono<ByteBuf> getBodyByteBuf(AmqpRequest request, ByteBufAllocator byteBufAllocator) {
        ByteBuf out = byteBufAllocator.ioBuffer();
        if (request instanceof BodyFrame) {
            out.writeByte(request.getType());
            out.writeShort(request.getChannel());

            writePayLoad(request, byteBufAllocator, out);

            out.writeByte(AMQP.FRAME_END);
        }
        return Mono.just(out);
    }

    private static void writePayLoad(AmqpRequest request, ByteBufAllocator byteBufAllocator, ByteBuf out) {
        AtomicInteger counter = new AtomicInteger();
        ByteBuf payload = byteBufAllocator.ioBuffer();

        request.writeValues(payload, counter);

        out.writeInt(counter.get());
        out.writeBytes(payload);
        payload.release();
    }
}
