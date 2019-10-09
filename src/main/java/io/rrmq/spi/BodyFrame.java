package io.rrmq.spi;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.FluxFinish;

import java.util.concurrent.atomic.AtomicInteger;

public class BodyFrame extends BaseFrame implements FluxFinish {

    private byte[] body;

    private BodyFrame(short type, short channel, byte[] body) {
        super(type, channel);
        this.body = body;
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

    public static BodyFrame of(short type, short channel, byte[] body) {
        return new BodyFrame(type, channel, body);
    }

}
