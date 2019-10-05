package io.rrmq.spi.method.transaction.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.transaction.SelectOk;

import static io.rrmq.spi.method.ProtocolClassType.TRANSACTION;
import static io.rrmq.spi.method.transaction.TransactionMethodType.SELECT_OK;

public class SelectOkAmqpMethod extends BaseFrame implements SelectOk {

    private SelectOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return TRANSACTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return SELECT_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "SelectOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new SelectOkAmqpMethod(type, channel);
    }

}
