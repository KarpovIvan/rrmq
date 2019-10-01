package io.rrmq.spi.method.channel;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.helper.LongString;

public interface ChannelOpenOk extends AmqpResponse {

    LongString getChannelId();

}
