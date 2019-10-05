package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.queue.QueueMethodType;
import io.rrmq.spi.method.queue.impl.*;

public class QueueMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch(QueueMethodType.valueOf(in.readShort())) {
            case DECLARE:
                return QueueDeclareAmqpMethod.of(type, channel, in);
            case DECLARE_OK:
                return QueueDeclareOkAmqpMethod.of(type, channel, in);
            case BIND:
                return QueueBindAmqpMethod.of(type, channel, in);
            case BIND_OK:
                return QueueBindOkAmqpMethod.of(type, channel);
            case PURGE:
                return QueuePurgeAmqpMethod.of(type, channel, in);
            case PURGE_OK:
                return QueuePurgeOkAmqpMethod.of(type, channel, in);
            case DELETE:
                return QueueDeleteAmqpMethod.of(type, channel, in);
            case DELETE_OK:
                return QueueDeleteOkAmqpMethod.of(type, channel, in);
            case UNBIND:
                return QueueUnbindAmqpMethod.of(type, channel, in);
            case UNBIND_OK:
                return QueueUnbindOkAmqpMethod.of(type, channel);
            default:
                return null;
        }
    }
}
