package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.Blocked;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.BLOCKED;

public class BlockedAmqpMethod extends BaseFrame implements Blocked {

    private final String reason;

    private BlockedAmqpMethod(short type, short channel, ByteBuf in) {
        this(type, channel, readShortstr(in));
    }

    private BlockedAmqpMethod(short type, short channel, String reason) {
        super(type, channel);
        this.reason = reason;
    }

    public String getReason() { return reason; }


    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(reason, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return BLOCKED.getDiscriminator();
    }

    @Override
    public String toString() {
        return "BlockedAmqpMethod{" +
                "reason='" + reason + '\'' +
                "} " + super.toString();
    }

    public static Blocked of(short type, short channel, ByteBuf in) {
        return new BlockedAmqpMethod(type, channel, in);
    }

    public static Blocked of(short type, short channel, String reason) {
        return new BlockedAmqpMethod(type, channel, reason);
    }
}
