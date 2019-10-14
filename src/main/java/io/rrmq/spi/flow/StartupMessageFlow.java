package io.rrmq.spi.flow;

import com.rabbitmq.client.impl.ClientVersion;
import io.rrmq.spi.AmqpRequest;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.Client;
import io.rrmq.spi.helper.LongStringHelper;
import io.rrmq.spi.method.connection.Start;
import io.rrmq.spi.method.connection.Tune;
import io.rrmq.spi.method.connection.impl.OpenAmqpMethod;
import io.rrmq.spi.method.connection.impl.ProtocolAmqpMethod;
import io.rrmq.spi.method.connection.impl.StartOkAmqpMethod;
import io.rrmq.spi.method.connection.impl.TuneOkAmqpMethod;
import io.rrmq.spi.sasl.DefaultSaslConfig;
import io.rrmq.spi.sasl.SaslMechanism;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.Map;

public class StartupMessageFlow {

    final static String COPYRIGHT = "Copyright (c) 2007-2019 Pivotal Software, Inc.";
    final static String LICENSE = "Licensed under the MPL. See https://www.rabbitmq.com/";

    public static Flux<AmqpResponse> exchange(Client client) {
        EmitterProcessor<AmqpRequest> requestProcessor = EmitterProcessor.create();
        FluxSink<AmqpRequest> requests = requestProcessor.sink();
        return client.exchange(requestProcessor.startWith(new ProtocolAmqpMethod()))
                .handle((response, sink) -> {
                    if (response instanceof Start) {
                        SaslMechanism saslMechanism = DefaultSaslConfig.PLAIN.getSaslMechanism(
                                ((Start) response).getMechanisms()
                                        .toString()
                                        .split(" ")
                        );
                        requests.next(
                                StartOkAmqpMethod.builder()
                                        .setChannel(response.getChannel())
                                        .setClientProperties(amqpProperties())
                                        .setMechanism(saslMechanism.getName())
                                        .setResponse(saslMechanism.handleChallenge(null, "guest", "guest"))
                                        .build()
                        );
                    } else if (response instanceof Tune) {
                        Tune tune = (Tune) response;
                        requests.next(
                                TuneOkAmqpMethod.builder()
                                        .setChannel(response.getChannel())
                                        .setChannelMax(tune.getChannelMax())
                                        .setFrameMax(tune.getFrameMax())
                                        .setHeartMax(tune.getHeartbeat())
                                        .build()
                        );
                        requests.next(
                                OpenAmqpMethod.builder()
                                        .setChannel(response.getChannel())
                                        .setVirtualHost("/")
                                        .setCapabilities("")
                                        .build()
                        );
                    } else {
                        requests.complete();
                    }
                });

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
