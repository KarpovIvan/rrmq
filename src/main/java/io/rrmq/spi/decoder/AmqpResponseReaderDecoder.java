package io.rrmq.spi.decoder;

import com.rabbitmq.client.AMQP;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.rrmq.spi.AmqpHolder;
import org.reactivestreams.Publisher;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class AmqpResponseReaderDecoder implements Function<ByteBuf, Publisher<AmqpHolder>> {

    private final CompositeByteBuf byteBuf;

    private final AtomicBoolean disposed = new AtomicBoolean();

    public AmqpResponseReaderDecoder(ByteBufAllocator byteBuf) {
        this.byteBuf = byteBuf.compositeBuffer();
    }

    @Override
    public Flux<AmqpHolder> apply(ByteBuf in) {
        //Assert.requireNonNull(in, "in must not be null");

        this.byteBuf.addComponent(true, in);
        this.byteBuf.retain();

        return EmitterProcessor.<AmqpHolder>create(sink -> {
            try {

                AmqpHolder envelope = getEnvelope(this.byteBuf);
                while (envelope != null) {
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

    static AmqpHolder getEnvelope(CompositeByteBuf in) {
        //Assert.requireNonNull(in, "in must not be null");

        int i = in.readableBytes();

        System.out.println("readableBytes " + i);

        if (i < 8) {
            System.out.println("i<3");
            return null;
        }

        int payloadSize1 = in.getInt(in.readerIndex() + 3);

        System.out.println(payloadSize1);

        if (i < payloadSize1 + 8) {
//            if (payloadSize1 > 10000) {
//                byte[] bytes = new byte[i];
//                in.readBytes(bytes);
//                System.out.println(new String(bytes));
//            }
            System.out.println("i = " + i + " < payloadSize1 = " + payloadSize1);
            return null;
        }

        AmqpHolder amqpHolder = new AmqpHolder();

        short type = in.readUnsignedByte();
        if (type == 'A') {
            protocolVersionMismatch(in);
        }
        amqpHolder.setType(type);

        int channel = in.readUnsignedShort();
        amqpHolder.setChannel((short) channel);

        int payloadSize = in.readInt();

        System.out.println("payloadSize = " + payloadSize);

        if (i == 23) {
            System.out.println();
        }

        return readComposite(in, payloadSize, amqpHolder);
    }

    static AmqpHolder readComposite(CompositeByteBuf in, int length, AmqpHolder amqpHolder) {
        if (length == 0) {
            amqpHolder.setPayLoad(in.alloc().compositeBuffer(1));
        } else {
            List<ByteBuf> decompose = in.decompose(in.readerIndex(), length);
            CompositeByteBuf byteBufs = in.alloc().compositeBuffer(decompose.size());
            for (ByteBuf byteBuf : decompose) {
                byteBufs.addComponent(true, byteBuf.retain());
            }
            in.readSlice(length);
            amqpHolder.setPayLoad(byteBufs);
        }

        int frameEndMarker = in.readUnsignedByte();
        if (frameEndMarker != AMQP.FRAME_END) {
            throw new RuntimeException();
        }
        return amqpHolder;
    }

    public void dispose() {
        if (this.disposed.compareAndSet(false, true)) {
            ReferenceCountUtil.release(this.byteBuf);
        }
    }

    public static void protocolVersionMismatch(ByteBuf is) {
        RuntimeException x;

        // We expect the letters M, Q, P in that order: generate an informative error if they're not found
        byte[] expectedBytes = new byte[]{'M', 'Q', 'P'};
        for (byte expectedByte : expectedBytes) {
            int nextByte = is.readUnsignedByte();
            if (nextByte != expectedByte) {
                throw new RuntimeException("Invalid AMQP protocol header from server: expected character " +
                        expectedByte + ", got " + nextByte);
            }
        }

        int[] signature = new int[4];

        for (int i = 0; i < 4; i++) {
            signature[i] = is.readUnsignedByte();
        }

        if (signature[0] == 1 &&
                signature[1] == 1 &&
                signature[2] == 8 &&
                signature[3] == 0) {
            x = new RuntimeException("AMQP protocol version mismatch; we are version " +
                    AMQP.PROTOCOL.MAJOR + "-" + AMQP.PROTOCOL.MINOR + "-" + AMQP.PROTOCOL.REVISION +
                    ", server is 0-8");
        } else {
            String sig = "";
            for (int i = 0; i < 4; i++) {
                if (i != 0) sig += ",";
                sig += signature[i];
            }

            x = new RuntimeException("AMQP protocol version mismatch; we are version " +
                    AMQP.PROTOCOL.MAJOR + "-" + AMQP.PROTOCOL.MINOR + "-" + AMQP.PROTOCOL.REVISION +
                    ", server sent signature " + sig);
        }

        throw x;
    }
}
