package io.rrmq.spi.method.сonfirm;

import io.rrmq.spi.AmqpRequest;

public interface Select extends AmqpRequest {
    boolean isNowait();
}
