package io.rrmq.spi.method.сonfirm.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.сonfirm.SelectOk;

import static io.rrmq.spi.method.ProtocolClassType.CONFIRM;
import static io.rrmq.spi.method.сonfirm.ConfirmMethodType.SELECT_OK;

public class SelectOkAmqpMethod extends BaseFrame implements SelectOk {

    private SelectOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return CONFIRM.getDiscriminator();
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
