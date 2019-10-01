package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.OpenOk;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.OPEN_OK;

public class OpenOkAmqpMethod extends BaseFrame implements OpenOk {

    private String knownHosts;

    private OpenOkAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readShortstr(in));
    }

    private OpenOkAmqpMethod(short type, short channel, String knownHosts) {
        super(type, channel);
        this.knownHosts = knownHosts;
    }

    @Override
    public String getKnownHosts() {
        return knownHosts;
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return OPEN_OK.getDiscriminator();
    }

    @Override
    public void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.knownHosts, out, counter);
    }

    public static OpenOk of(short type, short channel, ByteBuf in) {
        return new OpenOkAmqpMethod(type, channel, in);
    }

    public static OpenOk of(short type, short channel, String knownHosts) {
        return new OpenOkAmqpMethod(type, channel, knownHosts);
    }

    @Override
    public String toString() {
        return "OpenOkAmqpMethod{" +
                "knownHosts='" + knownHosts + '\'' +
                "} " + super.toString();
    }
}
