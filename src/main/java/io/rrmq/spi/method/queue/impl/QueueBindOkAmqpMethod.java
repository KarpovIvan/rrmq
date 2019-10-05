package io.rrmq.spi.method.queue.impl;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.queue.QueueBindOk;

import static io.rrmq.spi.method.ProtocolClassType.QUEUE;
import static io.rrmq.spi.method.queue.QueueMethodType.BIND_OK;

public class QueueBindOkAmqpMethod extends BaseFrame implements QueueBindOk {

    private QueueBindOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    public static AmqpResponse of(short type, short channel) {
        return new QueueBindOkAmqpMethod(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return QUEUE.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return BIND_OK.getDiscriminator();
    }

    @Override
    public String toString() {
        return "QueueBindOkAmqpMethod{} " + super.toString();
    }
}
