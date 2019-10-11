package io.rrmq.spi.method.channel.impl;

import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.BodyFrame;
import io.rrmq.spi.Client;
import io.rrmq.spi.flow.MessageFlow;
import io.rrmq.spi.header.BasicProperties;
import io.rrmq.spi.method.basic.Deliver;
import io.rrmq.spi.method.basic.impl.AckAmqpMethod;
import io.rrmq.spi.method.basic.impl.ConsumeAmqpMethod;
import io.rrmq.spi.method.basic.impl.PublishAmqpMethod;
import io.rrmq.spi.method.basic.impl.QosAmqpMethod;
import io.rrmq.spi.method.channel.Channel;
import io.rrmq.spi.method.exchange.ExchangeDeclare;
import io.rrmq.spi.method.queue.QueueBind;
import io.rrmq.spi.method.queue.QueueDeclare;
import io.rrmq.spi.method.сonfirm.impl.SelectAmqpMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.rrmq.spi.decoder.AmqpResponseDecoder.MessageType.FRAME_METHOD;

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

    @Override
    public Mono<Void> send(PublishAmqpMethod publishAmqpMethod, BasicProperties basicProperties, BodyFrame bodyFrame) {
        return MessageFlow.exchange(
                client,
                SelectAmqpMethod.of(FRAME_METHOD.getDiscriminator(), (short) 1, false),
                publishAmqpMethod,
                basicProperties,
                bodyFrame
        )
        .then();
    }

    @Override
    public Mono<Void> sendMany(Flux<AmqpRequest> requests) {
        return MessageFlow.exchange(
                client,
                requests
//                SelectAmqpMethod.of(FRAME_METHOD.getDiscriminator(), (short) 1, false),
//                publishAmqpMethod,
//                basicProperties,
//                bodyFrame
        )
        .then();
    }

    @Override
    public Flux<AmqpResponse> consume(ConsumeAmqpMethod consumeAmqpMethod) {
        return MessageFlow.exchange(
                client,
                QosAmqpMethod.builder()
                        .setChannel((short) 1)
                        .setGlobal(true)
                        .setPrefetchCount(40)
                        .build(),
                consumeAmqpMethod
        )
        .delayUntil(response -> {
            if (response instanceof Deliver) {
                return ack((Deliver) response);
            } else if(response instanceof BodyFrame) {
                System.out.println(new String(((BodyFrame) response).getBody()));
            }
            return Mono.just(response);
        });
    }

    @Override
    public Mono<Void> ack(Deliver deliver) {
        return MessageFlow.exchange(
                client,
                AckAmqpMethod.builder()
                        .setDeliveryTag(deliver.getDeliveryTag())
                        .setChannel((short) 1)
                        .build())
        .handle((response, synchronousSink) -> {
            System.out.println("COMP!!");
            synchronousSink.complete();
        }).then();
    }
}
