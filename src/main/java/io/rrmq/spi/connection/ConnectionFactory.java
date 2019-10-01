package io.rrmq.spi.connection;

import org.reactivestreams.Publisher;

public interface ConnectionFactory {
    Publisher<? extends Connection> create();
}
