package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;

public interface Blocked extends AmqpResponse {
    String getReason();
}
