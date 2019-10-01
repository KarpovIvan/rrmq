package io.rrmq.spi.utils;


import static io.rrmq.spi.decoder.AmqpResponseDecoder.MessageType.FRAME_METHOD;

public abstract class AmqpBuilder<T extends AmqpBuilder<T, R>, R> {

    private short channel;

    private short type = (short) FRAME_METHOD.getDiscriminator();

    public T setChannel(short channel) {
        this.channel = channel;
        return self();
    }

    public T setType(short type) {
        this.type = type;
        return self();
    }

    public short getChannel() {
        return channel;
    }

    public short getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public abstract R build();

}


