package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.QosOk;

import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.QOS_OK;

public class QosOkAmqpMethod extends BaseFrame implements QosOk {

    private QosOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return QOS_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "QosOkAmqpMethod{} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new QosOkAmqpMethod(type, channel);
    }

}
