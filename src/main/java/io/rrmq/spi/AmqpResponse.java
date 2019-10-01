package io.rrmq.spi;

public interface AmqpResponse {

    short getProtocolClassId();

    short getProtocolMethodId();

    short getChannel();

    short getType();


}
