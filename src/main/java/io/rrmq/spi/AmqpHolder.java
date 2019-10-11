package io.rrmq.spi;

import io.netty.buffer.CompositeByteBuf;

public class AmqpHolder {

    private short type;

    private short channel;

    private CompositeByteBuf payLoad;

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getChannel() {
        return channel;
    }

    public void setChannel(short channel) {
        this.channel = channel;
    }

    public CompositeByteBuf getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(CompositeByteBuf payLoad) {
        this.payLoad = payLoad;
    }
}
