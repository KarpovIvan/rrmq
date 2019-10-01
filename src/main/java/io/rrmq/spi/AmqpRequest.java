package io.rrmq.spi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;

import java.util.concurrent.atomic.AtomicInteger;

public interface AmqpRequest {

    short getProtocolClassId();

    short getProtocolMethodId();

    short getChannel();

    short getType();

    void writeValues(ByteBuf out, AtomicInteger counter);

    Publisher<ByteBuf> encode(ByteBufAllocator byteBufAllocator);


}
