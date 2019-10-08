package io.rrmq.spi.connection;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.channel.Channel;
import io.rrmq.spi.method.channel.ChannelOpen;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Connection {

    Mono<Channel> createChannel();

    Mono<Void> close();

}
