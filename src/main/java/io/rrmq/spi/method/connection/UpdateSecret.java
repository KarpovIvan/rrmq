package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.helper.LongString;

public interface UpdateSecret extends AmqpResponse {

    LongString getNewSecret();
    String getReason();

}
