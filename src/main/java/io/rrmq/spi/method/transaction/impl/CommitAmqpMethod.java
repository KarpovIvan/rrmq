package io.rrmq.spi.method.transaction.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.ProtocolClassType;
import io.rrmq.spi.method.transaction.Commit;
import io.rrmq.spi.method.transaction.TransactionMethodType;

import static io.rrmq.spi.method.ProtocolClassType.TRANSACTION;
import static io.rrmq.spi.method.transaction.TransactionMethodType.*;

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
