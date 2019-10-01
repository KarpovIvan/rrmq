package io.rrmq.spi.method.channel;

import io.rrmq.spi.AmqpResponse;

public interface ChannelClose extends AmqpResponse {

    int getReplyCode();
    String getReplyText();
    int getClassId();
    int getMethodId();

}
