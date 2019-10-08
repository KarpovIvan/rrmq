package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.exchange.ExchangeMethodType;
import io.rrmq.spi.method.exchange.impl.*;

public class ExchangeMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch (ExchangeMethodType.valueOf(in.readShort())) {
            case DECLARE:
                return ExchangeDeclareAmqpMethod.of(type, channel, in);
            case DECLARE_OK:
                return DeclareOkAmqpMethod.of(type, channel);
            case DELETE:
                return DeleteAmqpMethod.of(type, channel, in);
            case DELETE_OK:
                return DeleteOkAmqpMethod.of(type, channel);
            case BIND:
                return ExchangeBindAmqpMethod.of(type, channel, in);
            case BIND_OK:
                return BindOkAmqpMethod.of(type, channel);
            case UNBIND:
                return UnbindAmqpMethod.of(type, channel, in);
            case UNBIND_OK:
                return UnbindOkAmqpMethod.of(type, channel);
            default:
                return null;
        }
    }

}
