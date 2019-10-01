package io.rrmq.spi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.rrmq.spi.helper.LongString;
import io.rrmq.spi.helper.LongStringHelper;
import io.rrmq.spi.method.AmqpReadUtils;
import io.rrmq.spi.method.AmqpWriteUtils;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AmqpWriteAndReadUtilsTest {

    @Test
    public void longString() {
        LongString source = LongStringHelper.asLongString("Test!");

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeLongstr(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        LongString target = AmqpReadUtils.readLongstr(buffer);

        assertEquals(target, source);
    }

    @Test
    public void octet() {
        char source = 'S';

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeOctet(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        short target = AmqpReadUtils.readOctet(buffer);

        assertEquals(target, source);
    }

    @Test
    public void minLong() {
        int source = 10;

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeLong(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        int target = AmqpReadUtils.readLong(buffer);

        assertEquals(target, source);
    }

    @Test
    public void maxLong() {
        long source = 10L;

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeLonglong(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        long target = AmqpReadUtils.readLonglong(buffer);

        assertEquals(target, source);
    }

    @Test
    public void shortString() {
        String source = "Test!";

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeShortstr(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        String target = AmqpReadUtils.readShortstr(buffer);

        assertEquals(target, source);
    }

    @Test
    public void timestamp() {
        Date source = new Date(1569493001000L);

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeTimestamp(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        Date target = AmqpReadUtils.readTimestamp(buffer);

        assertEquals(target, source);
    }

    @Test
    public void _short() {
        short source = 10;

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeShort(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        short target = AmqpReadUtils.readShort(buffer);

        assertEquals(target, source);
    }

    @Test
    public void _int() {
        int source = 10;

        ByteBuf buffer = Unpooled.buffer();
        AmqpWriteUtils.writeInt(source, buffer, new AtomicInteger());

        assertTrue(buffer.isReadable());

        int target = AmqpReadUtils.readInt(buffer);

        assertEquals(target, source);
    }

    @Test
    public void test_Buffer() {
        ByteBuf buffer = Unpooled.buffer();

        HashMap<String, Object> source = new HashMap<>();
        source.put("Stret", "Stret1");
        source.put("skot", "Stret2");
        source.put("brayen", "Stret3");
        source.put("poul", "Stret4");
        source.put("ogurchik", "Stret5");

        AmqpWriteUtils.writeTable(source, buffer, new AtomicInteger());

        AmqpWriteUtils.writeInt(44, buffer, new AtomicInteger());
        AmqpWriteUtils.writeShort((short) 3, buffer, new AtomicInteger());

        Map<String, Object> target = AmqpReadUtils.readTable(buffer);

        assertEquals(44, AmqpReadUtils.readInt(buffer));
        assertEquals(3, AmqpReadUtils.readShort(buffer));

    }



}
