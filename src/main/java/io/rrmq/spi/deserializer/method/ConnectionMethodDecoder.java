package io.rrmq.spi.deserializer.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.connection.ConnectionMethodType;
import io.rrmq.spi.method.connection.impl.*;

public class ConnectionMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch(ConnectionMethodType.valueOf(in.readShort())) {
            case START:
                return StartAmqpMethod.of(type, channel, in);
            case START_OK:
                return StartOkAmqpMethod.of(type, channel, in);
            case SECURE:
                return SecureAmqpMethod.of(type, channel, in);
            case SECURE_OK:
                return SecureOkAmqpMethod.of(type, channel, in);
            case TUNE:
                return TuneAmqpMethod.of(type, channel, in);
            case TUNE_OK:
                return TuneOkAmqpMethod.of(type, channel, in);
            case OPEN:
                return OpenAmqpMethod.of(type, channel, in);
            case OPEN_OK:
                return OpenOkAmqpMethod.of(type, channel, in);
            case CLOSE:
                return CloseAmqpMethod.of(type, channel, in);
            case CLOSE_OK:
                return CloseOkAmqpMethod.of(type, channel);
            case BLOCKED:
                return BlockedAmqpMethod.of(type, channel, in);
            case UN_BLOCKED:
                return UnblockedAmqpMethod.of(type, channel);
            case UPDATE_SECRET:
                return UpdateSecretAmqpMethod.of(type, channel, in);
            case UPDATE_SECRET_OK:
                return UpdateSecretOkAmqpMethod.of(type, channel);
            default:
                return null;
        }
    }
}
