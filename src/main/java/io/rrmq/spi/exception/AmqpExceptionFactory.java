package io.rrmq.spi.exception;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.connection.CloseOk;
import reactor.core.publisher.SynchronousSink;

public class AmqpExceptionFactory {

    public static void handleErrorResponse(AmqpResponse response, SynchronousSink<AmqpResponse> sink) {
        if (!(response instanceof CloseOk)) {
            sink.next(response);
            return;
        }
    }
}
