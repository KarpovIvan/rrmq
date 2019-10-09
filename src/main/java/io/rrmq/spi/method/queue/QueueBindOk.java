package io.rrmq.spi.method.queue;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.connection.FluxFinish;

public interface QueueBindOk extends AmqpResponse, FluxFinish {
}
