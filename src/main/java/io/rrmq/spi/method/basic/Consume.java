package io.rrmq.spi.method.basic;

import java.util.Map;

public interface Consume {
    int getTicket();
    String getQueue();
    String getConsumerTag();
    boolean isNoLocal();
    boolean isNoAck();
    boolean isExclusive();
    boolean isNowait();
    Map<String,Object> getArguments();
}
