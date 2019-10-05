package io.rrmq.spi.method.transaction.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.transaction.Rollback;

import static io.rrmq.spi.method.ProtocolClassType.TRANSACTION;
import static io.rrmq.spi.method.transaction.TransactionMethodType.ROLLBACK;

public class RollbackAmqpMethod extends BaseFrame implements Rollback {

    private RollbackAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return TRANSACTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return ROLLBACK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "RollbackAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new RollbackAmqpMethod(type, channel);
    }

}
