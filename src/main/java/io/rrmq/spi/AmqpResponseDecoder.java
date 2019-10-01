package io.rrmq.spi;

import com.rabbitmq.client.AMQP;
import io.netty.buffer.ByteBuf;
import io.rrmq.spi.deserializer.ProtocolClassDecode;


public class AmqpResponseDecoder {

    public static AmqpResponse decode(ByteBuf in) {
        short type = in.readUnsignedByte();
        if (type == 'A') {
            protocolVersionMismatch(in);
        }
        int channel = in.readUnsignedShort();
        return deserializeBody(type, (short) channel, in);
    }

    private static AmqpResponse deserializeBody(short type, short channel, ByteBuf in) {
        switch (MessageType.valueOf(type)) {
            case FRAME_METHOD:
                return ProtocolClassDecode.decode(type, channel, in);
//            case FRAME_HEADER:
//            case FRAME_BODY:
//            case FRAME_HEARTBEAT:
//            case FRAME_MIN_SIZE:
//            case FRAME_END:
//            case REPLY_SUCCESS:
//            case CONTENT_TOO_LARGE:
//            case NO_ROUTE:
//            case NO_CONSUMERS:
//            case ACCESS_REFUSED:
//            case NOT_FOUND:
//            case RESOURCE_LOCKED:
//            case PRECONDITION_FAILED:
//            case CONNECTION_FORCED:
//            case INVALID_PATH:
//            case FRAME_ERROR:
//            case SYNTAX_ERROR:
//            case COMMAND_INVALID:
//            case CHANNEL_ERROR:
//            case UNEXPECTED_FRAME:
//            case RESOURCE_ERROR:
//            case NOT_ALLOWED:
//            case NOT_IMPLEMENTED:
//            case INTERNAL_ERROR:
            default:
                return null;
        }
    }

    private enum CAState {
        EXPECTING_METHOD,
        EXPECTING_CONTENT_HEADER,
        EXPECTING_CONTENT_BODY,
        COMPLETE
    }

    public enum MessageType {

        FRAME_METHOD(1),
        FRAME_HEADER(2),
        FRAME_BODY(3),
        FRAME_HEARTBEAT(8),
        FRAME_MIN_SIZE(4096),
        FRAME_END(206),
        REPLY_SUCCESS(200),
        CONTENT_TOO_LARGE(311),
        NO_ROUTE(312),
        NO_CONSUMERS(313),
        ACCESS_REFUSED(403),
        NOT_FOUND(404),
        RESOURCE_LOCKED(405),
        PRECONDITION_FAILED(406),
        CONNECTION_FORCED(320),
        INVALID_PATH(402),
        FRAME_ERROR(501),
        SYNTAX_ERROR(502),
        COMMAND_INVALID(503),
        CHANNEL_ERROR(504),
        UNEXPECTED_FRAME(505),
        RESOURCE_ERROR(506),
        NOT_ALLOWED(530),
        NOT_IMPLEMENTED(540),
        INTERNAL_ERROR(541);

        private final int discriminator;

        MessageType(int discriminator) {
            this.discriminator = discriminator;
        }

        public int getDiscriminator() {
            return discriminator;
        }

        static MessageType valueOf(int b) {
            for (MessageType messageType : values()) {
                if (messageType.discriminator == b) {
                    return messageType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }


    }

    private enum ConnectionMethodType {
        START(10),
        START_OK(11),
        SECURE(20),
        SECURE_OK(21),
        TUNE(30),
        TUNE_OK(31),
        OPEN(40),
        OPEN_OK(40),
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

        static ConnectionMethodType valueOf(int b) {
            for (ConnectionMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    private enum ChannelMethodType {
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

        static ChannelMethodType valueOf(int b) {
            for (ChannelMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    private enum AccessMethodType {
        REQUEST(10),
        REQUEST_OK(11);

        private final int discriminator;

        AccessMethodType(int discriminator) {
            this.discriminator = discriminator;
        }

        static AccessMethodType valueOf(int b) {
            for (AccessMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    private enum ExchangeMethodType {
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

        static ExchangeMethodType valueOf(int b) {
            for (ExchangeMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    private enum QueueMethodType {
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

        static QueueMethodType valueOf(int b) {
            for (QueueMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    private enum BasicMethodType {
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

        static BasicMethodType valueOf(int b) {
            for (BasicMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    private enum TransactionMethodType {
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

        static TransactionMethodType valueOf(int b) {
            for (TransactionMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    private enum ConfirmMethodType {
        SELECT(10),
        SELECT_OK(11);

        private final int discriminator;

        ConfirmMethodType(int discriminator) {
            this.discriminator = discriminator;
        }

        static ConfirmMethodType valueOf(int b) {
            for (ConfirmMethodType methodType : values()) {
                if (methodType.discriminator == b) {
                    return methodType;
                }
            }
            throw new IllegalArgumentException(String.format("%c is not a valid message type", b));
        }
    }

    public static void protocolVersionMismatch(ByteBuf is) {
        RuntimeException x;

        // We expect the letters M, Q, P in that order: generate an informative error if they're not found
        byte[] expectedBytes = new byte[]{'M', 'Q', 'P'};
        for (byte expectedByte : expectedBytes) {
            int nextByte = is.readUnsignedByte();
            if (nextByte != expectedByte) {
                throw new RuntimeException("Invalid AMQP protocol header from server: expected character " +
                        expectedByte + ", got " + nextByte);
            }
        }

        int[] signature = new int[4];

        for (int i = 0; i < 4; i++) {
            signature[i] = is.readUnsignedByte();
        }

        if (signature[0] == 1 &&
                signature[1] == 1 &&
                signature[2] == 8 &&
                signature[3] == 0) {
            x = new RuntimeException("AMQP protocol version mismatch; we are version " +
                    AMQP.PROTOCOL.MAJOR + "-" + AMQP.PROTOCOL.MINOR + "-" + AMQP.PROTOCOL.REVISION +
                    ", server is 0-8");
        } else {
            String sig = "";
            for (int i = 0; i < 4; i++) {
                if (i != 0) sig += ",";
                sig += signature[i];
            }

            x = new RuntimeException("AMQP protocol version mismatch; we are version " +
                    AMQP.PROTOCOL.MAJOR + "-" + AMQP.PROTOCOL.MINOR + "-" + AMQP.PROTOCOL.REVISION +
                    ", server sent signature " + sig);
        }

        throw x;
    }

}
