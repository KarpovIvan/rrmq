package io.rrmq.spi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.rrmq.spi.method.BaseFrame;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyFrame extends BaseFrame {

    private byte[] body;

    private BodyFrame(short type, short channel, byte[] body) {
        super(type, channel);
        this.body = body;
    }

    public BodyFrame(short type, short channel, CompositeByteBuf body) {
        super(type, channel);
        this.body = new byte[body.readableBytes()];
        body.readBytes(this.body);
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public short getProtocolClassId() {
        return 0;
    }

    @Override
    public short getProtocolMethodId() {
        return 0;
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) {
        counter.addAndGet(body.length);
        out.writeBytes(body);
    }

    @Override
    public String toString() {
        return "BodyFrame{" +
                "body=" + new String(body) +
                "} " + super.toString();
    }

    public static BodyFrame of(short type, short channel, byte[] body) {
        return new BodyFrame(type, channel, body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BodyFrame))
            return false;
        BodyFrame bodyFrame = (BodyFrame) o;

        if (!Arrays.equals(body, bodyFrame.body)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(body);
        return 31 * (result ^ (result >>> 16));
    }
}
