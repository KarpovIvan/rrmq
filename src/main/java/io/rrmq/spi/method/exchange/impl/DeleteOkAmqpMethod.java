package io.rrmq.spi.method.exchange.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.exchange.DeleteOk;

import static io.rrmq.spi.method.ProtocolClassType.EXCHANGE;
import static io.rrmq.spi.method.exchange.ExchangeMethodType.DELETE_OK;

public class DeleteOkAmqpMethod extends BaseFrame implements DeleteOk {

    private DeleteOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return EXCHANGE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DELETE_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "DeleteOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new DeleteOkAmqpMethod(type, channel);
    }

}
