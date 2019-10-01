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
