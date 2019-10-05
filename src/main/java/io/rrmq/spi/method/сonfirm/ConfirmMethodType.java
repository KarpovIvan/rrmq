package io.rrmq.spi.method.сonfirm;

public enum  ConfirmMethodType {
    SELECT(10),
    SELECT_OK(11);

    private final int discriminator;

    ConfirmMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static ConfirmMethodType valueOf(int b) {
        for (ConfirmMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }
}
