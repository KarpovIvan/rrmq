package io.rrmq.spi.method.queue.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueueBindOk;

import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.UNBIND_OK;

public class QueueUnbindOkAmqpMethod extends BaseFrame implements QueueBindOk {

    private QueueUnbindOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    public static AmqpResponse of(short type, short channel) {
        return new QueueUnbindOkAmqpMethod(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return QUEUE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return UNBIND_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "QueueUnbindOkAmqpMethod{} " + super.toString();
    }
}
