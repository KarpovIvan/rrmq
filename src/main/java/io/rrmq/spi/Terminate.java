/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rrmq.spi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * The Termination message.
 */
public final class Terminate implements AmqpRequest {

    /**
     * A static singleton instance that should always be used.
     */
    public static final Terminate INSTANCE = new Terminate();

    private Terminate() {
    }

    @Override
    public short getProtocolClassId() {
        return 0;
    }

    @Override
    public short getProtocolMethodId() {
        return 0;
    }

    @Override
    public short getChannel() {
        return 0;
    }

    @Override
    public short getType() {
        return 0;
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) {

    }

    @Override
    public Publisher<ByteBuf> encode(ByteBufAllocator byteBufAllocator) {
        //Assert.requireNonNull(byteBufAllocator, "byteBufAllocator must not be null");

        return Mono.defer(() -> {
            ByteBuf out = byteBufAllocator.ioBuffer(1 + 4);

            writeByte(out, 'X');
            out.writeInt(0);
            out.setInt(1, out.writerIndex() - 1);
            return Mono.just(out);
        });
    }

    static ByteBuf writeByte(ByteBuf out, int... values) {

        for (int value : values) {
            out.writeByte(value);
        }
        return out;
    }


    @Override
    public String toString() {
        return "Terminate{}";
    }

}
