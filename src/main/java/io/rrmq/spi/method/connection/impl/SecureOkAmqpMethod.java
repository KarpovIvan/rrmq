package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.SecureOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLongstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLongstr;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.SECURE_OK;

public class SecureOkAmqpMethod extends BaseFrame implements SecureOk {

    private final LongString response;

    private SecureOkAmqpMethod(short type, short channel, LongString response) {
        super(type, channel);
        this.response = response;
    }

    private SecureOkAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.response = readLongstr(in);
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLongstr(this.response, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return SECURE_OK.getDiscriminator();
    }

    @Override
    public LongString getResponse() {
        return response;
    }

    public static SecureOk of(short type, short channel, LongString response) {
        return new SecureOkAmqpMethod(type, channel, response);
    }

    public static SecureOk of(short type, short channel, ByteBuf in) {
        return new SecureOkAmqpMethod(type, channel, in);
    }

    @Override
    public String toString() {
        return "SecureOkAmqpMethod{" +
                "response=" + response +
                "} " + super.toString();
    }
}
