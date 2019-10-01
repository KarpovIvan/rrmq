package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.channel.ChannelMethodType;
import io.rrmq.spi.method.channel.impl.*;

public class ChannelMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch(ChannelMethodType.valueOf(in.readShort())) {
            case OPEN:
                return ChannelOpenAmqpMethod.of(type, channel, in);
            case OPEN_OK:
                return ChannelOpenOkAmqpMethod.of(type, channel, in);
            case FLOW:
                return ChannelFlowAmqpMethod.of(type, channel, in);
            case FLOW_OK:
                return ChannelFlowOkAmqpMethod.of(type, channel, in);
            case CLOSE:
                return ChannelCloseAmqpMethod.of(type, channel, in);
            case CLOSE_OK:
                return ChannelCloseOkAmqpMethod.of(type, channel);
            default:
                return null;
        }

    }

}
