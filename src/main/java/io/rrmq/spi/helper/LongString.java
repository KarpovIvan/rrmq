package io.rrmq.spi.helper;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface LongString {

    public static final long MAX_LENGTH = 0xffffffffL;

    /**
     * @return the length of the string in bytes between 0 and MAX_LENGTH (inclusive)
     */
    public long length();

    /**
     * Get the content stream.
     * Repeated calls to this function return the same stream,
     * which may not support rewind.
     * @return An input stream that reads the content of the string
     * @throws IOException if an error is encountered
     */
    public ByteBuf getStream();

    /**
     * Get the content as a byte array.  This need not be a copy. Updates to the
     * returned array may change the value of the string.
     * Repeated calls to this function may return the same array.
     * This function will fail if this string's length is greater than {@link Integer#MAX_VALUE},
     * throwing an {@link IllegalStateException}.
     * @return the array of bytes containing the content of the {@link com.rabbitmq.client.LongString}
     */
    public byte [] getBytes();

    /**
     * Get the content as a String. Uses UTF-8 as encoding.
     * @return he content of the {@link com.rabbitmq.client.LongString} as a string
     */
    @Override
    public String toString();

}
