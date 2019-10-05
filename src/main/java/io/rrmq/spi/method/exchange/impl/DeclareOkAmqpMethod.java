package io.rrmq.spi.method.exchange.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.exchange.DeclareOk;

import static io.rrmq.spi.method.ProtocolClassType.EXCHANGE;
import static io.rrmq.spi.method.exchange.ExchangeMethodType.DECLARE_OK;

public class DeclareOkAmqpMethod extends BaseFrame implements DeclareOk {

    private DeclareOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return EXCHANGE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return DECLARE_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "DeclareOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new DeclareOkAmqpMethod(type, channel);
    }

}
