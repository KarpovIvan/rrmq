package io.rrmq.spi.method.transaction;

public enum TransactionMethodType {
    SELECT(10),
    SELECT_OK(11),
    COMMIT(20),
    COMMIT_OK(21),
    ROLLBACK(30),
    ROLLBACK_OK(31);

    private final int discriminator;

    TransactionMethodType(int discriminator) {
        this.discriminator = discriminator;
    }

    public short getDiscriminator() {
        return (short) discriminator;
    }

    public static TransactionMethodType valueOf(int b) {
        for (TransactionMethodType methodType : values()) {
            if (methodType.discriminator == b) {
                return methodType;
            }
        }
        throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
    }
}
