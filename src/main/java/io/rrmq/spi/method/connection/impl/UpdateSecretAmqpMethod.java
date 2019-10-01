package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.UpdateSecret;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.readLongstr;
import static io.rrmq.spi.method.AmqpReadUtils.readShortstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeLongstr;
import static io.rrmq.spi.method.AmqpWriteUtils.writeShortstr;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.UPDATE_SECRET_OK;

public class UpdateSecretAmqpMethod extends BaseFrame implements UpdateSecret {

    private final LongString newSecret;
    private final String reason;

    private UpdateSecretAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.newSecret = readLongstr(in);
        this.reason = readShortstr(in);
    }

    private UpdateSecretAmqpMethod(UpdateSecretBuilder<?> builder) {
        super(builder.getType(), builder.getChannel());
        this.newSecret = builder.newSecret;
        this.reason = builder.reason;
    }

    @Override
    protected void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeLongstr(this.newSecret, out, counter);
        writeShortstr(this.reason, out, counter);
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return UPDATE_SECRET_OK.getDiscriminator();
    }

    @Override
    public LongString getNewSecret() {
        return newSecret;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "UpdateSecretAmqpMethod{" +
                "newSecret=" + newSecret +
                ", reason='" + reason + '\'' +
                "} " + super.toString();
    }

    public static class UpdateSecretBuilder<T extends UpdateSecretBuilder<T>> extends AmqpBuilder<T, UpdateSecret> {

        private LongString newSecret;
        private String reason;

        public T setNewSecret(LongString newSecret) {
            this.newSecret = newSecret;
            return self();
        }

        public T setReason(String reason) {
            this.reason = reason;
            return self();
        }

        @Override
        public UpdateSecret build() {
            return new UpdateSecretAmqpMethod(self());
        }
    }

    public static UpdateSecret of(short type, short channel, ByteBuf in) {
        return new UpdateSecretAmqpMethod(type, channel, in);
    }
}
