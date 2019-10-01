package io.rrmq.spi.helper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LongStringHelper {

    private static class ByteArrayLongString
            implements LongString
    {
        private final byte [] bytes;

        public ByteArrayLongString(byte[] bytes)
        {
            this.bytes = bytes;
        }

        @Override public boolean equals(Object o)
        {
            if(o instanceof LongString) {
                LongString other = (LongString)o;
                return Arrays.equals(this.bytes, other.getBytes());
            }

            return false;
        }

        @Override public int hashCode()
        {
            return Arrays.hashCode(this.bytes);
        }

        /** {@inheritDoc} */
        @Override
        public byte[] getBytes()
        {
            return bytes;
        }

        /** {@inheritDoc} */
        @Override
        public ByteBuf getStream() {
            return Unpooled.wrappedBuffer(bytes);
        }

        /** {@inheritDoc} */
        @Override
        public long length()
        {
            return bytes.length;
        }

        @Override
        public String toString()
        {
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    /**
     * Converts a String to a LongString using UTF-8 encoding.
     * @param string the string to wrap
     * @return a LongString wrapping it
     */
    public static LongString asLongString(String string)
    {
        if (string == null) return null;

        return new ByteArrayLongString(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts a binary block to a LongString.
     * @param bytes the data to wrap
     * @return a LongString wrapping it
     */
    public static LongString asLongString(byte [] bytes)
    {
        if (bytes==null) return null;
        return new ByteArrayLongString(bytes);
    }
}
