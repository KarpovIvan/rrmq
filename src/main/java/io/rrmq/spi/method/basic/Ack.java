package io.rrmq.spi.method.basic;

import io.rrmq.spi.method.connection.FluxFinish;

public interface Ack extends FluxFinish {
    long getDeliveryTag();
    boolean isMultiple();
}
