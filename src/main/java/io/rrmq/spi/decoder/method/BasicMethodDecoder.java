package io.rrmq.spi.decoder.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.AmqpResponse;
import io.rrmq.spi.method.basic.BasicMethodType;
import io.rrmq.spi.method.basic.impl.*;

public class BasicMethodDecoder {

    public static AmqpResponse decode(short type, short channel, ByteBuf in) {
        switch (BasicMethodType.valueOf(in.readShort())) {
            case QOS:
                return QosAmqpMethod.of(type, channel, in);
            case QOS_OK:
                return QosOkAmqpMethod.of(type, channel, in);
            case CONSUME:
                return ConsumeAmqpMethod.of(type, channel, in);
            case CONSUME_OK:
                return ConsumeOkAmqpMethod.of(type, channel, in);
            case CANCEL:
                return CancelAmqpMethod.of(type, channel, in);
            case CANCEL_OK:
                return CancelOkAmqpMethod.of(type, channel, in);
            case PUBLISH:
                return PublishAmqpMethod.of(type, channel, in);
            case RETURN:
                return ReturnAmqpMethod.of(type, channel, in);
            case DELIVERY:
                return DeliverAmqpMethod.of(type, channel, in);
            case GET:
                return GetAmqpMethod.of(type, channel, in);
            case GET_OK:
                return GetOkAmqpMethod.of(type, channel, in);
            case GET_EMPTY:
                return GetEmptyAmqpMethod.of(type, channel, in);
            case ACK:
                return AckAmqpMethod.of(type, channel, in);
            case REJECT:
                return RejectAmqpMethod.of(type, channel, in);
            case RECOVER_ASYNC:
                return RecoverAsyncAmqpMethod.of(type, channel, in);
            case RECOVER:
                return RecoverAmqpMethod.of(type, channel, in);
            case RECOVER_OK:
                return RecoverOkAmqpMethod.of(type, channel);
            case NACK:
                return NackAmqpMethod.of(type, channel, in);
            default:
                return null;
        }

    }
}
