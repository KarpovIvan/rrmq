package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.Start;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.START;

public class StartAmqpMethod extends BaseFrame implements Start {

    private int versionMajor;
    private int versionMinor;
    private Map<String,Object> serverProperties;
    private LongString mechanisms;
    private LongString locales;

    private StartAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.versionMajor = readOctet(in);
        this.versionMinor = readOctet(in);
        this.serverProperties = readTable(in);
        this.mechanisms = readLongstr(in);
        this.locales = readLongstr(in);
    }

    @Override
    public int getVersionMajor() {
        return versionMajor;
    }

    @Override
    public int getVersionMinor() {
        return versionMinor;
    }

    @Override
    public Map<String, Object> getServerProperties() {
        return serverProperties;
    }

    @Override
    public LongString getMechanisms() {
        return mechanisms;
    }

    @Override
    public LongString getLocales() {
        return locales;
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return START.getDiscriminator();
    }

    @Override
    public void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeOctet(this.versionMajor, out, counter);
        writeOctet(this.versionMinor, out, counter);
        writeTable(this.serverProperties, out, counter);
        writeLongstr(this.mechanisms, out, counter);
        writeLongstr(this.locales, out, counter);
    }

    public static Start of(short type, short channel, ByteBuf in) {
        return new StartAmqpMethod(type, channel, in);
    }

    @Override
    public String toString() {
        return "StartAmqpMethod{" +
                "versionMajor=" + versionMajor +
                ", versionMinor=" + versionMinor +
                ", serverProperties=" + serverProperties +
                ", mechanisms=" + mechanisms +
                ", locales=" + locales +
                "} " + super.toString();
    }
}
