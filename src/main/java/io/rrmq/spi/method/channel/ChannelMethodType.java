package io.rrmq.spi.method.channel;

public enum ChannelMethodType {

    OPEN(10),
    OPEN_OK(11),
    FLOW(20),
    FLOW_OK(21),
    CLOSE(40),
    CLOSE_OK(41);

    private final int discriminator;

    ChannelMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static ChannelMethodType valueOf(int b) {
        for (ChannelMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }

}
