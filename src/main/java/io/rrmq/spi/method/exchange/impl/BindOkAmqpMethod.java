package io.rrmq.spi.method.exchange.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.exchange.BindOk;

import static io.rrmq.spi.method.ProtocolClassType.EXCHANGE;
import static io.rrmq.spi.method.exchange.ExchangeMethodType.BIND_OK;

public class BindOkAmqpMethod extends BaseFrame implements BindOk {

    private BindOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return EXCHANGE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return BIND_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "BindOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new BindOkAmqpMethod(type, channel);
    }

}
