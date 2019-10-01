package io.rrmq.spi.exception;

import io.rrmq.spi.AmqpResponse;
import reactor.core.publisher.SynchronousSink;

public class AmqpExceptionFactory {

    public static void handleErrorResponse(AmqpResponse message, SynchronousSink<AmqpResponse> sink) {

    }
}
