package io.rrmq.spi.channel;

import java.time.Duration;

//Test Mode
public class ChannelPoolConfiguration {

    public Duration getMaxCreateChannelTime() {
        return Duration.ofDays(7);
    }

    public int getInitialSize() {
        return 0;
    }

    public int getMaxSize() {
        return 600;
    }

    public Duration getMaxAcquireTime() {
        return Duration.ofDays(7);
    }
}
