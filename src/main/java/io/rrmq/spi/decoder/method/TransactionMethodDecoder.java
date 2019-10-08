package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.transaction.TransactionMethodType;
import io.rrmq.spi.method.transaction.impl.*;

public class TransactionMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch (TransactionMethodType.valueOf(in.readShort())) {
            case SELECT:
                return SelectAmqpMethod.of(type, channel);
            case SELECT_OK:
                return SelectOkAmqpMethod.of(type, channel);
            case COMMIT:
                return CommitAmqpMethod.of(type, channel);
            case COMMIT_OK:
                return CommitOkAmqpMethod.of(type, channel);
            case ROLLBACK:
                return RollbackAmqpMethod.of(type, channel);
            case ROLLBACK_OK:
                return RollbackOkAmqpMethod.of(type, channel);
            default:
                return null;
        }
    }

}
