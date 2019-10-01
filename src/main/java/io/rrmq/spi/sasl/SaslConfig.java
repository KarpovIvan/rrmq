package io.rrmq.spi.sasl;

public interface SaslConfig {
    SaslMechanism getSaslMechanism(String[] mechanisms);
}
