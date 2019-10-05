package io.rrmq.spi.method.transaction.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.transaction.Select;

import static io.rrmq.spi.method.ProtocolClassType.TRANSACTION;
import static io.rrmq.spi.method.transaction.TransactionMethodType.SELECT;

public class SelectAmqpMethod extends BaseFrame implements Select {

    private SelectAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return TRANSACTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return SELECT.getDiscriminator();
    }

    @Override
    public String toString() {
        return "SelectAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new SelectAmqpMethod(type, channel);
    }

}
