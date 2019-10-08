package io.rrmq.spi.method.channel;

import io.rrmq.spi.method.exchange.ExchangeBind;
import io.rrmq.spi.method.exchange.ExchangeDeclare;
import io.rrmq.spi.method.queue.QueueBind;
import io.rrmq.spi.method.queue.QueueDeclare;
import reactor.core.publisher.Mono;

public interface Channel {

    Mono<Void> declareQueue(QueueDeclare queueDeclare);

    Mono<Void> declareExchange(ExchangeDeclare exchangeDeclare);

    Mono<Void> declareBinding(QueueBind queueBind);

}
