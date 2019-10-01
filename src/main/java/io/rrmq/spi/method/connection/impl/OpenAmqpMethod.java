package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.Open;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeBit;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.OPEN;

public class OpenAmqpMethod extends BaseFrame implements Open {

    private String virtualHost;
    private String capabilities;
    private boolean insist;

    private OpenAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.virtualHost = readShortstr(in);
        this.capabilities = readShortstr(in);
        this.insist = in.readBoolean();
    }

    private OpenAmqpMethod(OpenBuilder<?> openBuilder) {
        super(openBuilder.getType(), openBuilder.getChannel());
        this.virtualHost = openBuilder.virtualHost;
        this.capabilities = openBuilder.capabilities;
        this.insist = openBuilder.insist;
    }

    public static OpenBuilder<?> builder() {
        return new OpenBuilder<>();
    }


    @Override
    public String getVirtualHost() {
        return virtualHost;
    }

    @Override
    public String getCapabilities() {
        return capabilities;
    }

    @Override
    public boolean getInsist() {
        return insist;
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return OPEN.getDiscriminator();
    }

    @Override
    public String toString() {
        return "OpenAmqpMethod{" +
                "virtualHost='" + virtualHost + '\'' +
                ", capabilities='" + capabilities + '\'' +
                ", insist=" + insist +
                "} " + super.toString();
    }

    public static class OpenBuilder<T extends OpenBuilder<T>> extends AmqpBuilder<T, OpenAmqpMethod> {

        private String virtualHost;
        private String capabilities;
        private boolean insist;

        public T setVirtualHost(String virtualHost) {
            this.virtualHost = virtualHost;
            return self();
        }

        public T setCapabilities(String capabilities) {
            this.capabilities = capabilities;
            return self();
        }

        public T setInsist(boolean insist) {
            this.insist = insist;
            return self();
        }

        @Override
        public OpenAmqpMethod build() {
            return new OpenAmqpMethod(self());
        }
    }


    @Override
    public void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.virtualHost, out, counter);
        writeShortstr(this.capabilities, out, counter);
        writeBit(this.insist, out, counter);
    }

    public static Open of(short type, short channel, ByteBuf in) {
        return new OpenAmqpMethod(type, channel, in);
    }
}
