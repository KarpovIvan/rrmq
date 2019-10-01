package io.rrmq.spi.method.connection;


import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.helper.LongString;

public interface SecureOk extends AmqpResponse {

    LongString getResponse();

}
