package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.сonfirm.ConfirmMethodType;
import io.rrmq.spi.method.сonfirm.impl.SelectAmqpMethod;
import io.rrmq.spi.method.сonfirm.impl.SelectOkAmqpMethod;

public class ConfirmMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch (ConfirmMethodType.valueOf(in.readShort())) {
            case SELECT:
                SelectAmqpMethod.of(type, channel, in);
            case SELECT_OK:
                SelectOkAmqpMethod.of(type, channel);
            default:
                return null;
        }
    }

}
