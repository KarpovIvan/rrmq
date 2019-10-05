package io.rrmq.spi.method.queue;

public enum  QueueMethodType {

    DECLARE(10),
    DECLARE_OK(11),
    BIND(20),
    BIND_OK(21),
    PURGE(30),
    PURGE_OK(31),
    DELETE(40),
    DELETE_OK(41),
    UNBIND(50),
    UNBIND_OK(51);

    private final int discriminator;

    QueueMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static QueueMethodType valueOf(int b) {
        for (QueueMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }

}
