package io.rrmq.spi.method.transaction.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.transaction.RollbackOk;

import static io.rrmq.spi.method.ProtocolClassType.TRANSACTION;
import static io.rrmq.spi.method.transaction.TransactionMethodType.ROLLBACK_OK;

public class RollbackOkAmqpMethod extends BaseFrame implements RollbackOk {

    private RollbackOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return TRANSACTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return ROLLBACK_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "RollbackOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new RollbackOkAmqpMethod(type, channel);
    }

}
