package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.helper.LongString;

import java.util.Map;

public interface StartOk extends AmqpResponse {

    Map<String, Object> getClientProperties();
    String getMechanism();
    LongString getResponse();
    String getLocale();

}
