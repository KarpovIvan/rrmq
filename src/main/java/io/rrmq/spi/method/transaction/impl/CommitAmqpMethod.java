package io.rrmq.spi.method.transaction.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.transaction.Commit;

import static io.rrmq.spi.method.ProtocolClassType.TRANSACTION;
import static io.rrmq.spi.method.transaction.TransactionMethodType.COMMIT;

public class CommitAmqpMethod extends BaseFrame implements Commit {

    private CommitAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return TRANSACTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return COMMIT.getDiscriminator();
    }

    @Override
    public String toString() {
        return "CommitAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new CommitAmqpMethod(type, channel);
    }

}
