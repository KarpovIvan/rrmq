package io.rrmq.spi.method.exchange;

public enum ExchangeMethodType {
    DECLARE(10),
    DECLARE_OK(11),
    DELETE(20),
    DELETE_OK(21),
    BIND(30),
    BIND_OK(31),
    UNBIND(40),
    UNBIND_OK(51);

    private final int discriminator;

    ExchangeMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static ExchangeMethodType valueOf(int b) {
        for (ExchangeMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }
}
