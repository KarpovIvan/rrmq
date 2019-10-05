package io.rrmq.spi.method.transaction.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.transaction.CommitOk;

import static io.rrmq.spi.method.ProtocolClassType.TRANSACTION;
import static io.rrmq.spi.method.transaction.TransactionMethodType.COMMIT_OK;

public class CommitOkAmqpMethod extends BaseFrame implements CommitOk {

    private CommitOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return TRANSACTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return COMMIT_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "CommitOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new CommitOkAmqpMethod(type, channel);
    }

}
