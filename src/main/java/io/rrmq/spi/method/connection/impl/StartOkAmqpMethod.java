package io.rrmq.spi.method.connection.impl;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.method.connection.StartOk;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpReadUtils.*;
import static io.rrmq.spi.method.AmqpWriteUtils.*;
import static io.rrmq.spi.method.ProtocolClassType.CONNECTION;
import static io.rrmq.spi.method.connection.ConnectionMethodType.START_OK;

public class StartOkAmqpMethod extends BaseFrame implements StartOk {

    private final Map<String,Object> clientProperties;
    private final String mechanism;
    private final LongString response;
    private final String locale;

    private StartOkAmqpMethod(short type, short channel, ByteBuf in) {
        super(type, channel);
        this.clientProperties = readTable(in);
        this.mechanism = readShortstr(in);
        this.response = readLongstr(in);
        this.locale = readShortstr(in);
    }

    private StartOkAmqpMethod(StartOkBuilder<?> startOkBuilder) {
        super(startOkBuilder.getType(), startOkBuilder.getChannel());
        this.clientProperties = startOkBuilder.clientProperties;
        this.mechanism = startOkBuilder.mechanism;
        this.response = startOkBuilder.response;
        this.locale = startOkBuilder.locale;
    }

    public static StartOkBuilder<?> builder() {
        return new StartOkBuilder<>();
    }

    public static StartOk of(short type, short channel, ByteBuf in) {
        return new StartOkAmqpMethod(type, channel, in);
    }

    @Override
    public short getProtocolClassId() {
        return CONNECTION.getDiscriminator();
    }

    @Override
    public short getProtocolMethodId() {
        return START_OK.getDiscriminator();
    }

    @Override
    public Map<String, Object> getClientProperties() {
        return clientProperties;
    }

    @Override
    public String getMechanism() {
        return mechanism;
    }

    @Override
    public LongString getResponse() {
        return response;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public void writeMethodValues(ByteBuf out, AtomicInteger counter) {
        writeTable(clientProperties, out, counter);
        writeShortstr(mechanism, out, counter);
        writeLongstr(response, out, counter);
        writeShortstr(locale, out, counter);
    }

    @Override
    public String toString() {
        return "StartOkAmqpMethod{" +
                "clientProperties=" + clientProperties +
                ", mechanism='" + mechanism + '\'' +
                ", response=" + response +
                ", locale='" + locale + '\'' +
                "} " + super.toString();
    }

    public static class StartOkBuilder<T extends StartOkBuilder<T>> extends AmqpBuilder<T, StartOkAmqpMethod> {

        private Map<String,Object> clientProperties;
        private String mechanism = "PLAIN";
        private LongString response;
        private String locale = "en_US";

        public T setClientProperties(Map<String, Object> clientProperties) {
            this.clientProperties = clientProperties;
            return self();
        }

        public T setMechanism(String mechanism) {
            this.mechanism = mechanism;
            return self();
        }

        public T setResponse(LongString response) {
            this.response = response;
            return self();
        }

        public T setLocale(String locale) {
            this.locale = locale;
            return self();
        }

        @Override
        public StartOkAmqpMethod build() {
            return new StartOkAmqpMethod(self());
        }
    }
}
