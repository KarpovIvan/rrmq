package io.rrmq.spi.method.channel;

import io.rrmq.spi.BodyFrame;
import io.rrmq.spi.header.BasicProperties;
import io.rrmq.spi.method.basic.impl.PublishAmqpMethod;
import io.rrmq.spi.method.exchange.ExchangeBind;
import io.rrmq.spi.method.exchange.ExchangeDeclare;
import io.rrmq.spi.method.queue.QueueBind;
import io.rrmq.spi.method.queue.QueueDeclare;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Channel {

    Mono<Void> declareQueue(QueueDeclare queueDeclare);

    Mono<Void> declareExchange(ExchangeDeclare exchangeDeclare);

    Mono<Void> declareBinding(QueueBind queueBind);

    Mono<Void> send(PublishAmqpMethod publishAmqpMethod, BasicProperties basicProperties, BodyFrame bodyFrame);

}
