package io.rrmq.spi;


public class EndAmqpResponse implements AmqpResponse {
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
}
