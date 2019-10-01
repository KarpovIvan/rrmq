package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;

public interface Open extends AmqpResponse {

    String getVirtualHost();
    String getCapabilities();
    boolean getInsist();

}
