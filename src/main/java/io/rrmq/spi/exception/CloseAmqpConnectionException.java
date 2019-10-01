package io.rrmq.spi.exception;

public class CloseAmqpConnectionException extends RuntimeException {

    public CloseAmqpConnectionException() {
    }

    public CloseAmqpConnectionException(String message) {
        super(message);
    }

    public CloseAmqpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloseAmqpConnectionException(Throwable cause) {
        super(cause);
    }

    public CloseAmqpConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
