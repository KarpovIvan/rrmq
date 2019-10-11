package io.rrmq.spi.flow;

import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class MessageFlow {

    public static Flux<AmqpResponse> exchange(Client client, AmqpRequest queueDeclare) {
        return client.exchange(Mono.just(queueDeclare));
    }

    public static Flux<AmqpResponse> exchange(Client client, AmqpRequest... queueDeclare) {
        return client.exchange(Flux.just(queueDeclare));
    }

    public static Flux<AmqpResponse> exchange(Client client, Flux<AmqpRequest> amqpRequestFlux) {
        return client.exchange(amqpRequestFlux);
    }

}
