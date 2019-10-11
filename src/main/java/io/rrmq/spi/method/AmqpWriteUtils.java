package io.rrmq.spi.method;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.helper.LongString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AmqpWriteUtils {

    private AmqpWriteUtils() {
    }

    public static void writeTable(Map<String, Object> table, ByteBuf out, AtomicInteger counter) {
        counter.addAndGet(4);
        if (table == null) {
            out.writeInt(0);
        } else {
            out.writeInt((int) tableSize(table));
            for (Map.Entry<String, Object> entry : table.entrySet()) {
                writeShortstr(entry.getKey(), out, counter);
                Object value = entry.getValue();
                writeFieldValue(value, out, counter);
            }
        }
    }

    public static void writePresences(ByteBuf out, AtomicInteger counter, boolean... presents) {
        int flagWord = 0;
        int bitCount = 0;
        for (boolean present : presents) {
            if (bitCount == 15) {
                writeShort((short) (flagWord | 1), out, counter);
            }

            if (present) {
                int bit = 15 - bitCount;
                flagWord = flagWord | (1 << bit);
            }
            bitCount++;
        }
        writeShort((short) flagWord, out, counter);
    }


    public static void writeFieldValue(Object value, ByteBuf out, AtomicInteger counter) {
        if (value instanceof String) {
            writeOctet('S', out, counter);
            writeLongstr((String) value, out, counter);
        } else if (value instanceof LongString) {
            writeOctet('S', out, counter);
            writeLongstr((LongString) value, out, counter);
        } else if (value instanceof Integer) {
            writeOctet('I', out, counter);
            writeLong((Integer) value, out, counter);
        } else if (value instanceof BigDecimal) {
            writeOctet('D', out, counter);
            BigDecimal decimal = (BigDecimal) value;
            // The scale must be an unsigned octet, therefore its values must
            // be between 0 and 255
            if (decimal.scale() > 255 || decimal.scale() < 0)
                throw new IllegalArgumentException
                        ("BigDecimal has too large of a scale to be encoded. " +
                                "The scale was: " + decimal.scale());
            writeOctet(decimal.scale(), out, counter);
            BigInteger unscaled = decimal.unscaledValue();
            // We use 31 instead of 32 (Integer.SIZE) because bitLength ignores the sign bit,
            // so e.g. new BigDecimal(Integer.MAX_VALUE) comes out to 31 bits.
            if (unscaled.bitLength() > 31)
                throw new IllegalArgumentException
                        ("BigDecimal too large to be encoded");
            writeLong(decimal.unscaledValue().intValue(), out, counter);
        } else if (value instanceof Date) {
            writeOctet('T', out, counter);
            writeTimestamp((Date) value, out, counter);
        } else if (value instanceof Map) {
            writeOctet('F', out, counter);
            // Ignore the warnings here.  We hate erasure
            // (not even a little respect)
            // (We even have trouble recognising it.)
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            writeTable(map, out, counter);
        } else if (value instanceof Byte) {
            writeOctet('b', out, counter);
            counter.addAndGet(1);
            out.writeByte((Byte) value);
        } else if (value instanceof Double) {
            writeOctet('d', out, counter);
            out.writeDouble((Double) value);
        } else if (value instanceof Float) {
            writeOctet('f', out, counter);
            out.writeFloat((Float) value);
        } else if (value instanceof Long) {
            writeOctet('l', out, counter);
            out.writeLong((Long) value);
        } else if (value instanceof Short) {
            writeOctet('s', out, counter);
            out.writeShort((Short) value);
        } else if (value instanceof Boolean) {
            writeOctet('t', out, counter);
            counter.addAndGet(1);
            out.writeBoolean((Boolean) value);
        } else if (value instanceof byte[]) {
            writeOctet('x', out, counter);
            writeLong(((byte[]) value).length, out, counter);
            out.writeBytes((byte[]) value);
        } else if (value == null) {
            writeOctet('V', out, counter);
        } else if (value instanceof List) {
            writeOctet('A', out, counter);
            writeArray((List<?>) value, out, counter);
        } else if (value instanceof Object[]) {
            writeOctet('A', out, counter);
            writeArray((Object[]) value, out, counter);
        } else {
            throw new IllegalArgumentException("Invalid value type: " + value.getClass().getName());
        }
    }

    public static void writeOctet(int octet, ByteBuf byteBuf, AtomicInteger counter) {
        counter.addAndGet(1);
        byteBuf.writeByte(octet);
    }

    public static void writeBit(boolean octet, ByteBuf byteBuf, AtomicInteger counter) {
        counter.addAndGet(1);
        byteBuf.writeBoolean(octet);
    }

    public static void writeBits(ByteBuf out, AtomicInteger counter, boolean... octet) {
        byte bitAccumulator = 0;
        int bitMask = 1;
        for (boolean b : octet) {
            if (bitMask > 0x80) {
                writeByte(bitAccumulator, out, counter);
            }
            if (b) {
                bitAccumulator |= bitMask;
            }
            bitMask = bitMask << 1;
        }
        writeByte(bitAccumulator, out, counter);
    }

    public static void writeLongstr(LongString str, ByteBuf byteBuf, AtomicInteger counter) {
        int length = (int) str.length();
        writeLong(length, byteBuf, counter);
        counter.addAndGet(length);
        byteBuf.writeBytes(str.getBytes());
    }

    public static void writeLong(int l, ByteBuf byteBuf, AtomicInteger atomicInteger) {
        // java's arithmetic on this type is signed, however it's
        // reasonable to use ints to represent the unsigned long
        // type - for values < Integer.MAX_VALUE everything works
        // as expected
        atomicInteger.addAndGet(4);
        byteBuf.writeInt(l);
    }

    public static void writeLongstr(String str, ByteBuf byteBuf, AtomicInteger counter) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        writeLong(bytes.length, byteBuf, counter);
        counter.addAndGet(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public static void writeTimestamp(Date timestamp, ByteBuf byteBuf, AtomicInteger counter) {
        // AMQP uses POSIX time_t which is in seconds since the epoch began
        writeLonglong(timestamp.getTime() / 1000, byteBuf, counter);
    }

    public static void writeLonglong(long ll, ByteBuf out, AtomicInteger counter) {
        counter.addAndGet(8);
        out.writeLong(ll);
    }

    public static void writeShortstr(String str, ByteBuf out, AtomicInteger counter) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        if (length > 255) {
            throw new IllegalArgumentException(
                    "Short string too long; utf-8 encoded length = " + length +
                            ", max = 255.");
        }
        counter.addAndGet(1);
        out.writeByte(bytes.length);
        counter.addAndGet(bytes.length);
        out.writeBytes(bytes);
    }

    public static void writeShort(short s, ByteBuf out, AtomicInteger counter) {
        counter.addAndGet(2);
        out.writeShort(s);
    }

    public static void writeInt(int s, ByteBuf out, AtomicInteger counter) {
        counter.addAndGet(4);
        out.writeInt(s);
    }

    public static void writeByte(byte s, ByteBuf out, AtomicInteger counter) {
        counter.addAndGet(1);
        out.writeByte(s);
    }

    public static void writeArray(List<?> value, ByteBuf out, AtomicInteger counter) {
        if (value == null) {
            counter.addAndGet(1);
            out.writeByte(0);
        } else {
            counter.addAndGet(4);
            out.writeInt((int) arraySize(value));
            for (Object item : value) {
                writeFieldValue(item, out, counter);
            }
        }
    }

    public static void writeArray(Object[] value, ByteBuf out, AtomicInteger counter) {
        if (value == null) {
            counter.addAndGet(1);
            out.writeByte(0);
        } else {
            counter.addAndGet(4);
            out.writeInt((int) arraySize(value));
            for (Object item : value) {
                writeFieldValue(item, out, counter);
            }
        }
    }

    public static long tableSize(Map<String, Object> table) {
        long acc = 0;
        for (Map.Entry<String, Object> entry : table.entrySet()) {
            acc += shortStrSize(entry.getKey());
            acc += fieldValueSize(entry.getValue());
        }
        return acc;
    }

    /**
     * Computes the AMQP wire-protocol length of a protocol-encoded field-value.
     */
    private static long fieldValueSize(Object value) {
        long acc = 1; // for the type tag
        if (value instanceof String) {
            acc += longStrSize((String) value);
        } else if (value instanceof LongString) {
            acc += 4 + ((LongString) value).length();
        } else if (value instanceof Integer) {
            acc += 4;
        } else if (value instanceof BigDecimal) {
            acc += 5;
        } else if (value instanceof Date || value instanceof Timestamp) {
            acc += 8;
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            acc += 4 + tableSize(map);
        } else if (value instanceof Byte) {
            acc += 1;
        } else if (value instanceof Double) {
            acc += 8;
        } else if (value instanceof Float) {
            acc += 4;
        } else if (value instanceof Long) {
            acc += 8;
        } else if (value instanceof Short) {
            acc += 2;
        } else if (value instanceof Boolean) {
            acc += 1;
        } else if (value instanceof byte[]) {
            acc += 4 + ((byte[]) value).length;
        } else if (value instanceof List) {
            acc += 4 + arraySize((List<?>) value);
        } else if (value instanceof Object[]) {
            acc += 4 + arraySize((Object[]) value);
        } else if (value == null) {
        } else {
            throw new IllegalArgumentException("invalid value in table");
        }
        return acc;
    }

    public static long arraySize(List<?> values) {
        long acc = 0;
        for (Object value : values) {
            acc += fieldValueSize(value);
        }
        return acc;
    }

    public static long arraySize(Object[] values) {
        long acc = 0;
        for (Object value : values) {
            acc += fieldValueSize(value);
        }
        return acc;
    }

    private static int longStrSize(String str) {
        return str.getBytes(StandardCharsets.UTF_8).length + 4;
    }

    private static int shortStrSize(String str) {
        return str.getBytes(StandardCharsets.UTF_8).length + 1;
    }

}
