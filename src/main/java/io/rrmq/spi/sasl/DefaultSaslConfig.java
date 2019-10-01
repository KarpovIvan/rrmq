package io.rrmq.spi.sasl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultSaslConfig implements SaslConfig {

    private final String mechanism;

    public static final DefaultSaslConfig PLAIN = new DefaultSaslConfig("PLAIN");
    public static final DefaultSaslConfig EXTERNAL = new DefaultSaslConfig("EXTERNAL");


    private DefaultSaslConfig(String mechanism) {
        this.mechanism = mechanism;
    }

    @Override
    public SaslMechanism getSaslMechanism(String[] serverMechanisms) {
        Set<String> server = new HashSet<String>(Arrays.asList(serverMechanisms));

        if (server.contains(mechanism)) {
            if (mechanism.equals("PLAIN")) {
                return new PlainMechanism();
            }
            /*else if (mechanism.equals("EXTERNAL")) {
                return new ExternalMechanism();
            }*/
        }
        return null;
    }

}
