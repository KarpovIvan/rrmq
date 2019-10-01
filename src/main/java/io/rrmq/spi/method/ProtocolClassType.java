package io.rrmq.spi.method;

public enum ProtocolClassType {
    CONNECTION(10),
    CHANEL(20),
    ACCESS(30),
    EXCHANGE(40),
    QUEUE(50),
    BASIC(60),
    TRANSACTION(90),
    CONFIRM(85);

    private final int discriminator;

    ProtocolClassType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static ProtocolClassType valueOf(int b) {
        for (ProtocolClassType classType : values()) {
            if (classType.discriminator == b) {
                return classType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }

}
