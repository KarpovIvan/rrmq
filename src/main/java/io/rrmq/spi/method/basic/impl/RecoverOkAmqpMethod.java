package io.rrmq.spi.method.basic.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.RecoverOk;

import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.RECOVER_OK;

public class RecoverOkAmqpMethod extends BaseFrame implements RecoverOk {

    private RecoverOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return RECOVER_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "RecoverOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel) {
        return new RecoverOkAmqpMethod(type, channel);
    }

}
