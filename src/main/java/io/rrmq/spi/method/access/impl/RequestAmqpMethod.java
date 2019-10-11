package io.rrmq.spi.method.access.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.access.Request;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeBits;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.ACCESS;
import static io.rrmq.spi.method.access.AccessMethodType.REQUEST;

public class RequestAmqpMethod extends BaseFrame implements Request {
    private final String realm;
    private final boolean exclusive;
    private final boolean passive;
    private final boolean active;
    private final boolean write;
    private final boolean read;

    private RequestAmqpMethod(RequestAmqpBuilder<?> builder) {
        super(builder);
        this.realm = builder.realm;
        this.exclusive = builder.exclusive;
        this.passive = builder.passive;
        this.active = builder.active;
        this.write = builder.write;
        this.read = builder.read;
    }

    private RequestAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.realm = readShortstr(in);
        this.exclusive = in.readBoolean();
        this.passive = in.readBoolean();
        this.active = in.readBoolean();
        this.write = in.readBoolean();
        this.read = in.readBoolean();
    }

    @Override
    public String getRealm() {
        return realm;
    }

    @Override
    public boolean isExclusive() {
        return exclusive;
    }

    @Override
    public boolean isPassive() {
        return passive;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isWrite() {
        return write;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public short getProtocolClassId() {
        return ACCESS.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return REQUEST.getDiscriminator();
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) {
        writeShortstr(this.realm, out, counter);
        writeBits(out, counter, this.exclusive, this.passive, this.active, this.write, this.read);
    }

    @Override
    public String toString() {
        return "RequestAmqpMethod{" +
                "realm='" + realm + '\'' +
                ", exclusive=" + exclusive +
                ", passive=" + passive +
                ", active=" + active +
                ", write=" + write +
                ", read=" + read +
                "} " + super.toString();
    }

    public static AmqpResponse of(short type, short channel, ByteBuf in) {
        return new RequestAmqpMethod(type, channel, in);
    }

    public static class RequestAmqpBuilder<T extends RequestAmqpBuilder<T>> extends AmqpBuilder<T, RequestAmqpMethod> {

        private String realm;
        private boolean exclusive;
        private boolean passive;
        private boolean active;
        private boolean write;
        private boolean read;

        public T setRealm(String realm) {
            this.realm = realm;
            return self();
        }

        public T setExclusive(boolean exclusive) {
            this.exclusive = exclusive;
            return self();
        }

        public T setPassive(boolean passive) {
            this.passive = passive;
            return self();
        }

        public T setActive(boolean active) {
            this.active = active;
            return self();
        }

        public T setWrite(boolean write) {
            this.write = write;
            return self();
        }

        public T setRead(boolean read) {
            this.read = read;
            return self();
        }

        @Override
        public RequestAmqpMethod build() {
            return new RequestAmqpMethod(self());
        }
    }
    
}
