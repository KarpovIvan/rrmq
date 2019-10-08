package io.rrmq.spi.method.channel;

import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.method.connection.FluxFinish;

public interface ChannelOpenOk extends AmqpResponse, FluxFinish {

    LongString getChannelId();

}
