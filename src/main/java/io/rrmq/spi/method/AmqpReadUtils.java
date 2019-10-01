package io.rrmq.spi.method;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.helper.LongStringHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AmqpReadUtils {

    private static final long INT_MASK = 0xffffffffL;

//    public static AmqpMethod readMethodFrom(ByteBuf in) throws IOException {
//        int classId = in.readShort();
//        int methodId = in.readShort();
//        switch (classId) {
//            case 10:
//                switch (methodId) {
//                    case 10: {
//                        return new StartAmqpMethod(in);
//                    }
//                    case 11: {
//                        return new AMQImpl.Connection.StartOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 20: {
//                        return new AMQImpl.Connection.Secure(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 21: {
//                        return new AMQImpl.Connection.SecureOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 30: {
//                        return new AMQImpl.Connection.Tune(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 31: {
//                        return new AMQImpl.Connection.TuneOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 40: {
//                        return new AMQImpl.Connection.Open(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 41: {
//                        return new AMQImpl.Connection.OpenOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 50: {
//                        return new AMQImpl.Connection.Close(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 51: {
//                        return new AMQImpl.Connection.CloseOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 60: {
//                        return new AMQImpl.Connection.Blocked(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 61: {
//                        return new AMQImpl.Connection.Unblocked(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 70: {
//                        return new AMQImpl.Connection.UpdateSecret(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 71: {
//                        return new AMQImpl.Connection.UpdateSecretOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//            case 20:
//                switch (methodId) {
//                    case 10: {
//                        return new AMQImpl.Channel.Open(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 11: {
//                        return new AMQImpl.Channel.OpenOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 20: {
//                        return new AMQImpl.Channel.Flow(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 21: {
//                        return new AMQImpl.Channel.FlowOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 40: {
//                        return new AMQImpl.Channel.Close(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 41: {
//                        return new AMQImpl.Channel.CloseOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//            case 30:
//                switch (methodId) {
//                    case 10: {
//                        return new AMQImpl.Access.Request(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 11: {
//                        return new AMQImpl.Access.RequestOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//            case 40:
//                switch (methodId) {
//                    case 10: {
//                        return new AMQImpl.Exchange.Declare(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 11: {
//                        return new AMQImpl.Exchange.DeclareOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 20: {
//                        return new AMQImpl.Exchange.Delete(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 21: {
//                        return new AMQImpl.Exchange.DeleteOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 30: {
//                        return new AMQImpl.Exchange.Bind(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 31: {
//                        return new AMQImpl.Exchange.BindOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 40: {
//                        return new AMQImpl.Exchange.Unbind(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 51: {
//                        return new AMQImpl.Exchange.UnbindOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//            case 50:
//                switch (methodId) {
//                    case 10: {
//                        return new AMQImpl.Queue.Declare(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 11: {
//                        return new AMQImpl.Queue.DeclareOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 20: {
//                        return new AMQImpl.Queue.Bind(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 21: {
//                        return new AMQImpl.Queue.BindOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 30: {
//                        return new AMQImpl.Queue.Purge(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 31: {
//                        return new AMQImpl.Queue.PurgeOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 40: {
//                        return new AMQImpl.Queue.Delete(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 41: {
//                        return new AMQImpl.Queue.DeleteOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 50: {
//                        return new AMQImpl.Queue.Unbind(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 51: {
//                        return new AMQImpl.Queue.UnbindOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//            case 60:
//                switch (methodId) {
//                    case 10: {
//                        return new AMQImpl.Basic.Qos(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 11: {
//                        return new AMQImpl.Basic.QosOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 20: {
//                        return new AMQImpl.Basic.Consume(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 21: {
//                        return new AMQImpl.Basic.ConsumeOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 30: {
//                        return new AMQImpl.Basic.Cancel(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 31: {
//                        return new AMQImpl.Basic.CancelOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 40: {
//                        return new AMQImpl.Basic.Publish(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 50: {
//                        return new AMQImpl.Basic.Return(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 60: {
//                        return new AMQImpl.Basic.Deliver(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 70: {
//                        return new AMQImpl.Basic.Get(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 71: {
//                        return new AMQImpl.Basic.GetOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 72: {
//                        return new AMQImpl.Basic.GetEmpty(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 80: {
//                        return new AMQImpl.Basic.Ack(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 90: {
//                        return new AMQImpl.Basic.Reject(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 100: {
//                        return new AMQImpl.Basic.RecoverAsync(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 110: {
//                        return new AMQImpl.Basic.Recover(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 111: {
//                        return new AMQImpl.Basic.RecoverOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 120: {
//                        return new AMQImpl.Basic.Nack(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//            case 90:
//                switch (methodId) {
//                    case 10: {
//                        return new AMQImpl.Tx.Select(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 11: {
//                        return new AMQImpl.Tx.SelectOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 20: {
//                        return new AMQImpl.Tx.Commit(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 21: {
//                        return new AMQImpl.Tx.CommitOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 30: {
//                        return new AMQImpl.Tx.Rollback(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 31: {
//                        return new AMQImpl.Tx.RollbackOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//            case 85:
//                switch (methodId) {
//                    case 10: {
//                        return new AMQImpl.Confirm.Select(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    case 11: {
//                        return new AMQImpl.Confirm.SelectOk(new MethodArgumentReader(new ValueReader(in)));
//                    }
//                    default: break;
//                } break;
//        }
//
//        throw new UnknownClassOrMethodId(classId, methodId);
//    }


    static Object readFieldValue(ByteBuf in) {
        Object value = null;
        switch (in.readUnsignedByte()) {
            case 'S':
                value = readLongstr(in);
                break;
            case 'I':
                value = in.readInt();
                break;
            case 'D':
                int scale = in.readUnsignedByte();
                byte[] unscaled = new byte[4];
                in.readBytes(unscaled);
                value = new BigDecimal(new BigInteger(unscaled), scale);
                break;
            case 'T':
                value = readTimestamp(in);
                break;
            case 'F':
                value = readTable(in);
                break;
            case 'A':
                value = readArray(in);
                break;
            case 'b':
                value = in.readByte();
                break;
            case 'd':
                value = in.readDouble();
                break;
            case 'f':
                value = in.readFloat();
                break;
            case 'l':
                value = in.readLong();
                break;
            case 's':
                value = in.readShort();
                break;
            case 't':
                value = in.readBoolean();
                break;
            case 'x':
                value = readBytes(in);
                break;
            case 'V':
                value = null;
                break;
            default:
                throw new RuntimeException("Best of the Best@!");
        }
        return value;
    }

    private static List<Object> readArray(ByteBuf in) {
        try {
            ByteBufInputStream inputStream = new ByteBufInputStream(in, (int) unsignedExtend(in.readInt()));
            List<Object> array = new ArrayList<Object>();
            while (inputStream.available() > 0) {
                Object value = readFieldValue(in);
                array.add(value);
            }
            return array;
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    public static int readLong(ByteBuf in) {
        return in.readInt();
    }

    public static long readLonglong(ByteBuf in) {
        return in.readLong();
    }

    public static short readOctet(ByteBuf in) {
        return in.readUnsignedByte();
    }

    public static String readShortstr(ByteBuf in) {
        byte[] b = new byte[in.readUnsignedByte()];
        in.readBytes(b);
        return new String(b, StandardCharsets.UTF_8);
    }

    public static Date readTimestamp(ByteBuf in) {
        return new Date(in.readLong() * 1000);
    }

    public static short readShort(ByteBuf in) {
        return in.readShort();
    }

    public static int readInt(ByteBuf in) {
        return in.readInt();
    }


    public static Map<String, Object> readTable(ByteBuf in) {
        try {
            long tableLength = unsignedExtend(in.readInt());
            if (tableLength == 0) return Collections.emptyMap();

            Map<String, Object> table = new HashMap<>();
            ByteBufInputStream tableIn = new ByteBufInputStream(in, (int) tableLength);
            while (tableIn.available() > 0) {
                String name = readShortstr(in);
                Object value = readFieldValue(in);
                if (!table.containsKey(name))
                    table.put(name, value);
            }
            return table;
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    public static LongString readLongstr(ByteBuf in) {
        return LongStringHelper.asLongString(readBytes(in));
    }

    public static long unsignedExtend(int value) {
        long extended = value;
        return extended & INT_MASK;
    }

    private static byte[] readBytes(ByteBuf in) {
        final long contentLength = unsignedExtend(in.readInt());
        if (contentLength < Integer.MAX_VALUE) {
            final byte[] buffer = new byte[(int) contentLength];
            in.readBytes(buffer);
            return buffer;
        } else {
            throw new UnsupportedOperationException("Very long byte vectors and strings not currently supported");
        }
    }

}
