package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.access.AccessMethodType;
import io.rrmq.spi.method.access.impl.RequestAmqpMethod;
import io.rrmq.spi.method.access.impl.RequestOkAmqpMethod;

public class AccessMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch (AccessMethodType.valueOf(in.readShort())) {
            case REQUEST:
                RequestAmqpMethod.of(type, channel, in);
            case REQUEST_OK:
                RequestOkAmqpMethod.of(type, channel, in);
            default:
                return null;
        }
    }

}
