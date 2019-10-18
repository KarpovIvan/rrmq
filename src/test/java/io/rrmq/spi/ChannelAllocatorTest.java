package io.rrmq.spi;

import io.rrmq.spi.channel.ChannelAllocator;
import org.junit.Test;

public class ChannelAllocatorTest {

    @Test
    public void allocate() {
        ChannelAllocator channelAllocator = new ChannelAllocator(1, 1024);
        System.out.println(channelAllocator.reserve(channelAllocator.allocate()));
    }
}
