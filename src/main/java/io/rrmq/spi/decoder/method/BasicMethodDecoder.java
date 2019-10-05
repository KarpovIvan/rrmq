package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.basic.BasicMethodType;
import io.rrmq.spi.method.basic.impl.*;

public class BasicMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch (BasicMethodType.valueOf(in.readShort())) {
            case QOS:
                QosAmqpMethod.of(type, channel, in);
            case QOS_OK:
                QosOkAmqpMethod.of(type, channel, in);
            case CONSUME:
                ConsumeAmqpMethod.of(type, channel, in);
            case CONSUME_OK:
                ConsumeOkAmqpMethod.of(type, channel, in);
            case CANCEL:
                CancelAmqpMethod.of(type, channel, in);
            case CANCEL_OK:
                CancelOkAmqpMethod.of(type, channel, in);
            case PUBLISH:
                PublishAmqpMethod.of(type, channel, in);
            case RETURN:
                ReturnAmqpMethod.of(type, channel, in);
            case DELIVERY:
                DeliverAmqpMethod.of(type, channel, in);
            case GET:
                GetAmqpMethod.of(type, channel, in);
            case GET_OK:
                GetOkAmqpMethod.of(type, channel, in);
            case GET_EMPTY:
                GetEmptyAmqpMethod.of(type, channel, in);
            case ACK:
                AckAmqpMethod.of(type, channel, in);
            case REJECT:
                RejectAmqpMethod.of(type, channel, in);
            case RECOVER_ASYNC:
                RecoverAsyncAmqpMethod.of(type, channel, in);
            case RECOVER:
                RecoverAmqpMethod.of(type, channel, in);
            case RECOVER_OK:
                RecoverOkAmqpMethod.of(type, channel);
            case NACK:
                NackAmqpMethod.of(type, channel, in);
            default:
                return null;
        }

    }
}
