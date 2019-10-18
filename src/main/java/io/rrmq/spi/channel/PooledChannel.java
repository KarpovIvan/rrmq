package io.rrmq.spi.channel;

import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.BodyFrame;
import io.rrmq.spi.header.BasicProperties;
import io.rrmq.spi.method.basic.Deliver;
import io.rrmq.spi.method.basic.impl.ConsumeAmqpMethod;
import io.rrmq.spi.method.basic.impl.PublishAmqpMethod;
import io.rrmq.spi.method.exchange.ExchangeDeclare;
import io.rrmq.spi.method.queue.QueueBind;
import io.rrmq.spi.method.queue.QueueDeclare;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.pool.PooledRef;

public class PooledChannel implements Channel {

    private PooledRef<Channel> pooledRef;

    private Channel channel;

    public PooledChannel(PooledRef<Channel> channel) {
        this.pooledRef = channel;
        this.channel = channel.poolable();
    }

    @Override
    public Mono<Void> declareQueue(QueueDeclare queueDeclare) {
        return channel.declareQueue(queueDeclare);
    }

    @Override
    public Mono<Void> declareExchange(ExchangeDeclare exchangeDeclare) {
        return channel.declareExchange(exchangeDeclare);
    }

    @Override
    public Mono<Void> declareBinding(QueueBind queueBind) {
        return channel.declareBinding(queueBind);
    }

    @Override
    public Mono<Void> send(PublishAmqpMethod publishAmqpMethod, BasicProperties basicProperties, BodyFrame bodyFrame) {
       return channel.send(publishAmqpMethod, basicProperties, bodyFrame);
    }

    @Override
    public Mono<Void> sendMany(Flux<AmqpRequest> requests) {
        return channel.sendMany(requests);
    }

    @Override
    public Flux<AmqpResponse> consume(ConsumeAmqpMethod consumeAmqpMethod) {
        return channel.consume(consumeAmqpMethod);
    }

    @Override
    public Mono<Void> ack(Deliver deliver) {
        return channel.ack(deliver);
    }

    @Override
    public short channelId() {
        return channel.channelId();
    }
    @Override
    public Mono<Void> close() {
        return pooledRef.release();
    }

}
