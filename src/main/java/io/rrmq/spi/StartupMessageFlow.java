package io.rrmq.spi;

import com.rabbitmq.client.impl.ClientVersion;
import io.rrmq.spi.helper.LongStringHelper;
import io.rrmq.spi.method.connection.OpenOk;
import io.rrmq.spi.method.connection.Start;
import io.rrmq.spi.method.connection.Tune;
import io.rrmq.spi.method.connection.impl.*;
import io.rrmq.spi.sasl.DefaultSaslConfig;
import io.rrmq.spi.sasl.SaslMechanism;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.Map;

import static io.rrmq.spi.AmqpResponseDecoder.MessageType.REPLY_SUCCESS;

public class StartupMessageFlow {

    final static String COPYRIGHT = "Copyright (c) 2007-2019 Pivotal Software, Inc.";
    final static String LICENSE = "Licensed under the MPL. See https://www.rabbitmq.com/";

    public static Flux<AmqpResponse> exchange(Client client) {
        EmitterProcessor<AmqpRequest> requestProcessor = EmitterProcessor.create();
        FluxSink<AmqpRequest> sink = requestProcessor.sink();
        return client.exchange(requestProcessor.startWith(new ProtocolAmqpMethod()))
                .doOnNext(amqpResponse -> {
                    if (amqpResponse instanceof Start) {
                        sinkStartMethod(sink, amqpResponse);
                    } else if (amqpResponse instanceof Tune) {
                        sinkTuneAndOpen(sink, amqpResponse);
                    }
                });

    }

    private static void sinkTuneAndOpen(FluxSink<AmqpRequest> sink, AmqpResponse amqpResponse) {
        Tune tune = (Tune) amqpResponse;
        sink.next(
                TuneOkAmqpMethod.builder()
                        .setChannel(amqpResponse.getChannel())
                        .setChannelMax(tune.getChannelMax())
                        .setFrameMax(tune.getFrameMax())
                        .setHeartMax(tune.getHeartbeat())
                        .build()
        );
        sink.next(
                OpenAmqpMethod.builder()
                        .setChannel(amqpResponse.getChannel())
                        .setVirtualHost("/")
                        .setCapabilities("")
                        .build()
        );
    }

    private static void sinkStartMethod(FluxSink<AmqpRequest> sink, AmqpResponse amqpResponse) {
        SaslMechanism saslMechanism = DefaultSaslConfig.PLAIN.getSaslMechanism(
                ((Start) amqpResponse).getMechanisms()
                        .toString()
                        .split(" ")
        );
        sink.next(
                StartOkAmqpMethod.builder()
                        .setChannel(amqpResponse.getChannel())
                        .setClientProperties(amqpProperties())
                        .setMechanism(saslMechanism.getName())
                        .setResponse(saslMechanism.handleChallenge(null, "guest", "guest"))
                .build()
        );
    }


    private static Map<String, Object> amqpProperties() {
        Map<String,Object> props = new HashMap<String, Object>();
        props.put("product", LongStringHelper.asLongString("RabbitMQ"));
        props.put("version", LongStringHelper.asLongString(ClientVersion.VERSION));
        props.put("platform", LongStringHelper.asLongString("Java"));
        props.put("copyright", LongStringHelper.asLongString(COPYRIGHT));
        props.put("information", LongStringHelper.asLongString(LICENSE));

        Map<String, Object> capabilities = new HashMap<String, Object>();
        capabilities.put("publisher_confirms", true);
        capabilities.put("exchange_exchange_bindings", true);
        capabilities.put("basic.nack", true);
        capabilities.put("consumer_cancel_notify", true);
        capabilities.put("connection.blocked", true);
        capabilities.put("authentication_failure_close", true);

        props.put("capabilities", capabilities);
        return props;
    }
}
