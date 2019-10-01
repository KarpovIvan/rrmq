package io.rrmq.spi.sasl;


import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.helper.LongStringHelper;

public class PlainMechanism implements SaslMechanism {

    @Override
    public String getName() {
        return "PLAIN";
    }

    @Override
    public LongString handleChallenge(LongString challenge,
                                      String username,
                                      String password) {
        return LongStringHelper.asLongString("\0" + username +
                "\0" + password);
    }
}
