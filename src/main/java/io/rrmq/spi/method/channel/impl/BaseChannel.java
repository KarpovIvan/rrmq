package io.rrmq.spi.method.channel.impl;

import io.rrmq.spi.Client;
import io.rrmq.spi.flow.MessageFlow;
import io.rrmq.spi.method.channel.Channel;
import io.rrmq.spi.method.exchange.ExchangeBind;
import io.rrmq.spi.method.exchange.ExchangeDeclare;
import io.rrmq.spi.method.queue.QueueBind;
import io.rrmq.spi.method.queue.QueueDeclare;
import reactor.core.publisher.Mono;

public class BaseChannel implements Channel {

    private Client client;

    public BaseChannel(Client client) {
        this.client = client;
    }

    @Override
    public Mono<Void> declareQueue(QueueDeclare queueDeclare) {
        return MessageFlow.exchange(client, queueDeclare).then();
    }

    @Override
    public Mono<Void> declareExchange(ExchangeDeclare exchangeDeclare) {
        return MessageFlow.exchange(client, exchangeDeclare).then();
    }

    @Override
    public Mono<Void> declareBinding(QueueBind queueBind) {
        return MessageFlow.exchange(client, queueBind).then();
    }
}