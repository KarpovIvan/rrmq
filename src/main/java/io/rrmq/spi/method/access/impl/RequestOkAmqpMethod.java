package io.rrmq.spi.method.access.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.access.RequestOk;

import static io.rrmq.spi.method.AmqpReadUtils.readShort;
import static io.rrmq.spi.method.ProtocolClassType.ACCESS;
import static io.rrmq.spi.method.access.AccessMethodType.REQUEST_OK;

public class RequestOkAmqpMethod extends BaseFrame implements RequestOk {

    private final int ticket;

    private RequestOkAmqpMethod(short type, short channel, int ticket) {
        super(type, channel);
        this.ticket = ticket;
    }

    private RequestOkAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readShort(in));
    }

    @Override
    public int getTicket() {
        return this.ticket;
    }

    @Override
    public short getProtocolClassId() {
        return ACCESS.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return REQUEST_OK.getDiscriminator();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new RequestOkAmqpMethod(type, channel, in);
    }

    public static AmqpResponse of(short type, short channel, int ticket) {
        return new RequestOkAmqpMethod(type, channel, ticket);
    }

    @Override
    public String toString() {
        return "RequestOkAmqpMethod{" +
                "ticket=" + ticket +
                "} " + super.toString();
    }

}
