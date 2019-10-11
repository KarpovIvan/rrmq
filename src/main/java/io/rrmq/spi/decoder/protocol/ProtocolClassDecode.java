package io.rrmq.spi.decoder.protocol;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.decoder.method.*;
import io.rrmq.spi.method.ProtocolClassType;

public class ProtocolClassDecode {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch(ProtocolClassType.valueOf(in.readShort())) {
            case CONNECTION:
                return ConnectionMethodDecoder.decode(type, channel, in);
            case CHANEL:
                return ChannelMethodDecoder.decode(type, channel, in);
            case ACCESS:
                return AccessMethodDecoder.decode(type, channel, in);
            case EXCHANGE:
                return ExchangeMethodDecoder.decode(type, channel, in);
            case QUEUE:
                return QueueMethodDecoder.decode(type, channel, in);
            case BASIC:
                return BasicMethodDecoder.decode(type, channel, in);
            case TRANSACTION:
                return TransactionMethodDecoder.decode(type, channel, in);
            case CONFIRM:
                return ConfirmMethodDecoder.decode(type, channel, in);
            default:
                return null;
        }
    }

}
