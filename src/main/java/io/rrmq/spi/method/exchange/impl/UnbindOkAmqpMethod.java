package io.rrmq.spi.method.exchange.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.exchange.UnbindOk;

import static io.rrmq.spi.method.ProtocolClassType.EXCHANGE;
import static io.rrmq.spi.method.exchange.ExchangeMethodType.UNBIND_OK;

public class UnbindOkAmqpMethod extends BaseFrame implements UnbindOk {

    private UnbindOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return EXCHANGE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return UNBIND_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "UnbindOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new UnbindOkAmqpMethod(type, channel);
    }

}
