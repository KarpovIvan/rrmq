package io.rrmq.spi.method.channel.impl;

import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.channel.ChannelCloseOk;

import static io.rrmq.spi.method.ProtocolClassType.CHANEL;
import static io.rrmq.spi.method.channel.ChannelMethodType.CLOSE_OK;

public class ChannelCloseOkAmqpMethod extends BaseFrame implements ChannelCloseOk {

    private ChannelCloseOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return CHANEL.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return CLOSE_OK.getDiscriminator();
    }

    public static ChannelCloseOk of(short type, short channel) {
        return new ChannelCloseOkAmqpMethod(type, channel);
    }

    @Override
    public String toString() {
        return "ChannelCloseOkAmqpMethod{} " + super.toString();
    }
}
