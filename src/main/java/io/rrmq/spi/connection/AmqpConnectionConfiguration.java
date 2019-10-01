package io.rrmq.spi.connection;

public class AmqpConnectionConfiguration {

    private final String host;

    private final int port;

    public AmqpConnectionConfiguration(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
