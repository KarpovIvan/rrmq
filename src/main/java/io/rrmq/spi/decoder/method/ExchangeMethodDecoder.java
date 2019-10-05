package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.exchange.ExchangeMethodType;
import io.rrmq.spi.method.exchange.impl.*;

public class ExchangeMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch (ExchangeMethodType.valueOf(in.readShort())) {
            case DECLARE:
                DeclareAmqpMethod.of(type, channel, in);
            case DECLARE_OK:
                DeclareOkAmqpMethod.of(type, channel);
            case DELETE:
                DeleteAmqpMethod.of(type, channel, in);
            case DELETE_OK:
                DeleteOkAmqpMethod.of(type, channel);
            case BIND:
                BindAmqpMethod.of(type, channel, in);
            case BIND_OK:
                BindOkAmqpMethod.of(type, channel);
            case UNBIND:
                UnbindAmqpMethod.of(type, channel, in);
            case UNBIND_OK:
                UnbindOkAmqpMethod.of(type, channel);
            default:
                return null;
        }
    }

}
