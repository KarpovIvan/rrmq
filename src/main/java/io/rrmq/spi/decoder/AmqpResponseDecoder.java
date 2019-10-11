package io.rrmq.spi.decoder;

import io.netty.buffer.CompositeByteBuf;
import io.rrmq.spi.AmqpHolder;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.BodyFrame;
import io.rrmq.spi.EndAmqpResponse;
import io.rrmq.spi.decoder.protocol.ProtocolClassDecode;
import io.rrmq.spi.header.BasicProperties;


public class AmqpResponseDecoder {

    public static AmqpResponse decode(AmqpHolder amqpHolder) {
        CompositeByteBuf payLoad = amqpHolder.getPayLoad();
        try {
            return deserializeBody(amqpHolder.getType(), amqpHolder.getChannel(), payLoad);
        } finally {
             payLoad.release();
        }
    }

    private static AmqpResponse deserializeBody(short type, short channel, CompositeByteBuf in) {
        switch (MessageType.valueOf(type)) {
            case FRAME_METHOD:
                return ProtocolClassDecode.decode(type, channel, in);
            case FRAME_HEADER:
                return BasicProperties.builder().build();
            case FRAME_BODY:
                return new BodyFrame(type, channel, in);
            case END:
                return new EndAmqpResponse();
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
        INTERNAL_ERROR(541),
        //test
        END(900);

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
            throw new IllegalArgumentException(String.format("%s is not a valid message type", b));
        }


    }

}
