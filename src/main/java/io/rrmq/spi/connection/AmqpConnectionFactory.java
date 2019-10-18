package io.rrmq.spi.connection;

import io.rrmq.spi.AmqpReactorNettyClient;
import io.rrmq.spi.Client;
import io.rrmq.spi.channel.AmqpChannelFactory;
import io.rrmq.spi.channel.ChannelPool;
import io.rrmq.spi.channel.ChannelPoolConfiguration;
import io.rrmq.spi.flow.StartupMessageFlow;
import javafx.scene.chart.ScatterChart;
import reactor.core.publisher.Mono;

public class AmqpConnectionFactory implements ConnectionFactory {

    private final Mono<? extends Client> clientFactory;

    private ChannelPoolConfiguration configuration;


    public AmqpConnectionFactory(AmqpConnectionConfiguration amqpConnectionConfiguration,
                                 ChannelPoolConfiguration configuration) {
        this.clientFactory = Mono.defer(() -> AmqpReactorNettyClient.connect(amqpConnectionConfiguration.getHost(), amqpConnectionConfiguration.getPort()).cast(Client.class));
        this.configuration = configuration;
    }

    @Override
    public Mono<AmqpConnection> create() {
        return this.clientFactory
                .delayUntil(StartupMessageFlow::exchange)
                .flatMap(client -> Mono.fromSupplier(
                        () -> new AmqpConnection(client,
                                new ChannelPool(configuration, new AmqpChannelFactory(client))
                        ))
                );
    }

}
