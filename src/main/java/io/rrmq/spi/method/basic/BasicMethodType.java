package io.rrmq.spi.method.basic;

public enum BasicMethodType {
    QOS(10),
    QOS_OK(11),
    CONSUME(20),
    CONSUME_OK(21),
    CANCEL(30),
    CANCEL_OK(31),
    PUBLISH(40),
    RETURN(50),
    DELIVERY(60),
    GET(70),
    GET_OK(71),
    GET_EMPTY(72),
    ACK(80),
    REJECT(90),
    RECOVER_ASYNC(100),
    RECOVER(110),
    RECOVER_OK(111),
    NACK(120);

    private final int discriminator;

    BasicMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static BasicMethodType valueOf(int b) {
        for (BasicMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }
}
