package io.rrmq.spi.flow;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.Client;
import io.rrmq.spi.method.channel.impl.ChannelOpenAmqpMethod;
import reactor.core.publisher.Mono;

import static io.rrmq.spi.decoder.AmqpResponseDecoder.MessageType.FRAME_METHOD;

public class ChannelMessageFlow {

    public static Mono<AmqpResponse> exchange(Client client) {
        return client.exchange(Mono.just(ChannelOpenAmqpMethod.of((short) FRAME_METHOD.getDiscriminator(), (short) 1, "")))
                .next();
    }

}
