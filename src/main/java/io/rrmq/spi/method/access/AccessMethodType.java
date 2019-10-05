package io.rrmq.spi.method.access;

public enum AccessMethodType {
    REQUEST(10),
    REQUEST_OK(11);

    private final int discriminator;

    AccessMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static AccessMethodType valueOf(int b) {
        for (AccessMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }
}
