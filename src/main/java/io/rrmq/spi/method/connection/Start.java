package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.helper.LongString;

import java.util.Map;

public interface Start extends AmqpResponse {

    int getVersionMajor();
    int getVersionMinor();
    Map<String,Object> getServerProperties();
    LongString getMechanisms();
    LongString getLocales();

}
