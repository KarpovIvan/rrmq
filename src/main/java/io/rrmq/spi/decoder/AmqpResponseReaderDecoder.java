package io.rrmq.spi.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.reactivestreams.Publisher;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class AmqpResponseReaderDecoder implements Function<ByteBuf, Publisher<CompositeByteBuf>> {

    private final CompositeByteBuf byteBuf;

    private final AtomicBoolean disposed = new AtomicBoolean();

    private final AtomicInteger test = new AtomicInteger();

    public AmqpResponseReaderDecoder(ByteBufAllocator byteBuf) {
        this.byteBuf = byteBuf.compositeBuffer();
    }

    @Override
    public Flux<CompositeByteBuf> apply(ByteBuf in) {
        //Assert.requireNonNull(in, "in must not be null");

        this.byteBuf.addComponent(true, in);
        this.byteBuf.retain();

        return EmitterProcessor.<CompositeByteBuf>create(sink -> {
            try {

                CompositeByteBuf envelope = getEnvelope(this.byteBuf);
                while (envelope != null) {
                    System.out.println(test.getAndIncrement());
                    sink.next(envelope);
                    envelope = getEnvelope(this.byteBuf);
                }
                sink.complete();
            } finally {
                this.byteBuf.discardReadComponents();
            }
        })
                .doFinally(s -> ReferenceCountUtil.release(this.byteBuf));
    }

    static CompositeByteBuf getEnvelope(CompositeByteBuf in) {
        //Assert.requireNonNull(in, "in must not be null");

        if (in.readableBytes() < 5) {
            return null;
        }

        int length = 3 + in.getInt(3) + 4 + 1;
        if (in.readableBytes() < length) {
            return null;
        }

        System.out.println(length);

        return readComposite(in, length);
    }

    static CompositeByteBuf readComposite(CompositeByteBuf in, int length) {
        if (length == 0) {
            return in.alloc().compositeBuffer(1);
        }
        List<ByteBuf> decompose = in.decompose(in.readerIndex(), length);
        CompositeByteBuf byteBufs = in.alloc().compositeBuffer(decompose.size());
        for (ByteBuf byteBuf : decompose) {
            byteBufs.addComponent(true, byteBuf.retain());
        }
        in.readSlice(length);
        return byteBufs;
    }

    public void dispose() {
        if (this.disposed.compareAndSet(false, true)) {
             ReferenceCountUtil.release(this.byteBuf);
        }
    }
}