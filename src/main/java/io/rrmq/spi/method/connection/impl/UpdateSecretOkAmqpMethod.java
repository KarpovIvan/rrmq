package io.rrmq.spi.method.connection.impl;

import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.UpdateSecretOk;

import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.UPDATE_SECRET_OK;

public class UpdateSecretOkAmqpMethod extends BaseFrame implements UpdateSecretOk {

    public UpdateSecretOkAmqpMethod(short type, short channel) {
        super(type, channel);
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return UPDATE_SECRET_OK.getDiscriminator();
    }

    public static UpdateSecretOk of(short type, short channel) {
        return new UpdateSecretOkAmqpMethod(type, channel);
    }

    @Override
    public String toString() {
        return "UpdateSecretOkAmqpMethod{} " + super.toString();
    }
}
