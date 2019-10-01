package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.Secure;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLongstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLongstr;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.SECURE;

public class SecureAmqpMethod extends BaseFrame implements Secure {

    private final LongString challenge;

    private SecureAmqpMethod(short type, short channel, LongString challenge) {
        super(type, channel);
        this.challenge = challenge;
    }

    private SecureAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.challenge = readLongstr(in);
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLongstr(this.challenge, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return SECURE.getDiscriminator();
    }

    @Override
    public LongString getChallenge() {
        return challenge;
    }

    public static Secure of(short type, short channel, LongString challenge) {
        return new SecureAmqpMethod(type, channel, challenge);
    }

    public static Secure of(short type, short channel, ByteBuf in) {
        return new SecureAmqpMethod(type, channel, in);
    }

    @Override
    public String toString() {
        return "SecureAmqpMethod{" +
                "challenge=" + challenge +
                "} " + super.toString();
    }
}
