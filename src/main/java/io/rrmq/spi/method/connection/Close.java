package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;

public interface Close extends AmqpResponse {

    int getReplyCode();
    String getReplyText();
    int getClassId();
    int getMethodId();

}
