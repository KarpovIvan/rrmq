package io.rrmq.spi.decoder.protocol;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.decoder.method.ChannelMethodDecoder;
import io.rrmq.spi.decoder.method.ConnectionMethodDecoder;
import io.rrmq.spi.method.ProtocolClassType;

public class ProtocolClassDecode {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        int payloadSize = in.readInt();
        switch(ProtocolClassType.valueOf(in.readShort())) {
            case CONNECTION:
                return ConnectionMethodDecoder.decode(type, channel, in);
            case CHANEL:
                return ChannelMethodDecoder.decode(type, channel, in);
//            case ACCESS:
//            case EXCHANGE:
//            case QUEUE:
//            case BASIC:
//            case TRANSACTION:
//            case CONFIRM:
            default:
                return null;
        }
    }

}