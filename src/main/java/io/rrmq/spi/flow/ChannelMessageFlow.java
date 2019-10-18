package io.rrmq.spi.flow;

import io.rrmq.spi.Client;
import io.rrmq.spi.channel.Channel;
import io.rrmq.spi.method.channel.ChannelOpenOk;
import io.rrmq.spi.channel.BaseChannel;
import io.rrmq.spi.method.channel.impl.ChannelOpenAmqpMethod;
import reactor.core.publisher.Mono;

import static io.rrmq.spi.decoder.AmqpResponseDecoder.MessageType.FRAME_METHOD;

public class ChannelMessageFlow {

    public static Mono<Channel> exchange(Client client, int channelId) {
        return client.exchange(Mono.just(ChannelOpenAmqpMethod.of(FRAME_METHOD.getDiscriminator(), (short) channelId, "")))
                .<Channel>flatMap(response -> {
                    if (response instanceof ChannelOpenOk) {
                        return Mono.just(new BaseChannel(client, response.getChannel()));
                    }
                    throw new RuntimeException();
                })
                .next();
    }

}
