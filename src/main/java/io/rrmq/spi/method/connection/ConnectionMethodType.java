package io.rrmq.spi.method.connection;

public enum ConnectionMethodType {
    START(10),
    START_OK(11),
    SECURE(20),
    SECURE_OK(21),
    TUNE(30),
    TUNE_OK(31),
    OPEN(40),
    OPEN_OK(41),
    CLOSE(50),
    CLOSE_OK(51),
    BLOCKED(60),
    UN_BLOCKED(61),
    UPDATE_SECRET(70),
    UPDATE_SECRET_OK(71);

    private final int discriminator;

    ConnectionMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static ConnectionMethodType valueOf(int b) {
        for (ConnectionMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }
}