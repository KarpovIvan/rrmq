package io.rrmq.spi.method.basic.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.basic.GetEmpty;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.BASIC;
import static io.rrmq.spi.method.basic.BasicMethodType.GET_EMPTY;

public class GetEmptyAmqpMethod extends BaseFrame implements GetEmpty {

    private final String clusterId;

    private GetEmptyAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readShortstr(in));
    }

    private GetEmptyAmqpMethod(short type, short channel, String clusterId) {
        super(type, channel);
        this.clusterId = clusterId;
    }

    @Override
    public String getClusterId() {
        return clusterId;
    }

    @Override
    public short getProtocolClassId() {
        return BASIC.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return GET_EMPTY.getDiscriminator();
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.clusterId, out, counter);
    }

    @Override
    public String toString() {
        return "GetEmptyAmqpMethod{" +
                "clusterId='" + clusterId + '\'' +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new GetEmptyAmqpMethod(type, channel, in);
    }

    public static AmqpResponse of(short type, short channel, String clusterId) {
        return new GetEmptyAmqpMethod(type, channel, clusterId);
    }

}
