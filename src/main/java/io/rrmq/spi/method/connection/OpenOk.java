package io.rrmq.spi.method.connection;

import io.rrmq.spi.AmqpResponse;

public interface OpenOk extends AmqpResponse, FluxFinish {

    String getKnownHosts();

}
