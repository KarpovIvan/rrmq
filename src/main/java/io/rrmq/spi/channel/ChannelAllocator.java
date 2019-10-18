package io.rrmq.spi.channel;

import java.util.BitSet;

public class ChannelAllocator {

    private final int channelMin;
    private final int channelMax;
    private final int numberOfFreeBits;

    private int lastIndex = 0;

    private final BitSet channelNumbers;

    public ChannelAllocator(int channelMin, int channelMax) {
        this.channelMin = channelMin;
        this.channelMax = channelMax;

        this.numberOfFreeBits = channelMax - channelMin;

        this.channelNumbers = new BitSet(this.numberOfFreeBits);
        this.channelNumbers.set(0, this.numberOfFreeBits);
    }

    public int allocate() {
        int setIndex = this.channelNumbers.nextSetBit(this.lastIndex);

        if (setIndex < 0) {
            setIndex = this.channelNumbers.nextSetBit(0);
        }

        if (setIndex < 0) return -1;

        this.lastIndex = setIndex;

        this.channelNumbers.clear(setIndex);
        return setIndex + this.channelMin;
    }

    public void free(int reservation) {
        this.channelNumbers.set(reservation - this.channelMin);
    }

    public boolean reserve(int reservation) {
        int index = reservation - this.channelMin;
        if (this.channelNumbers.get(index)) {
            this.channelNumbers.clear(index);
            return true;
        } else {
            return false;
        }
    }

}
