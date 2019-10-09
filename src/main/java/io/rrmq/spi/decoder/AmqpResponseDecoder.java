package io.rrmq.spi.decoder;

import com.rabbitmq.client.AMQP;
import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.decoder.protocol.ProtocolClassDecode;


public class AmqpResponseDecoder {

    public static AmqpResponse decode(ByteBuf in) {
        try {
            short type = in.readUnsignedByte();
            if (type == 'A') {
                protocolVersionMismatch(in);
            }
            System.out.println(true);
            int channel = in.readUnsignedShort();
            return deserializeBody(type, (short) channel, in);
        } finally {
             in.release();
        }
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

        public short getDiscriminator() {
            return (short) discriminator;
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
