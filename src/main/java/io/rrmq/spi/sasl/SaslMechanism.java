package io.rrmq.spi.sasl;

import io.rrmq.spi.helper.LongString;

public interface SaslMechanism {

    String getName();

    LongString handleChallenge(LongString challenge, String username, String password);
}
