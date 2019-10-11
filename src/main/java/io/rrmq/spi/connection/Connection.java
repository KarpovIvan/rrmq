package io.rrmq.spi.connection;

import io.rrmq.spi.method.channel.Channel;
import reactor.core.publisher.Mono;

public interface Connection {

    Mono<Channel> createChannel();

    Mono<Void> close();

}
