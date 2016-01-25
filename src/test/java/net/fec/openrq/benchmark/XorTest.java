/*
 * Copyright 2014 OpenRQ Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.fec.openrq.benchmark;


import static net.fec.openrq.util.io.ByteBuffers.BufferType.ARRAY_BACKED;
import static net.fec.openrq.util.io.ByteBuffers.BufferType.DIRECT;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import net.fec.openrq.common.TestingCommon;

import net.fec.openrq.util.datatype.SizeOf;
import net.fec.openrq.util.datatype.UnsignedTypes;
import net.fec.openrq.util.io.ByteBuffers;
import net.fec.openrq.util.math.OctetOps;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;


@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
@Fork(0)
@State(Scope.Benchmark)
public class XorTest {

    @Param({SizeOf.LONG + "",
            SizeOf.LONG + "0",
            SizeOf.LONG + "00",
            SizeOf.LONG + "000",
            SizeOf.LONG + "0000",
            SizeOf.LONG + "00000",
    // SizeOf.LONG + "000000",
    // SizeOf.LONG + "0000000"
    })
    public int size;

    private ByteBuffer srcBuf;
    private ByteBuffer dstBuf;

    private ByteBuffer srcDirBuf;
    private ByteBuffer dstDirBuf;


    @Setup
    public void setup() {

        srcBuf = ByteBuffers.allocate(size, ARRAY_BACKED);
        dstBuf = ByteBuffers.allocate(size, ARRAY_BACKED);
        randomBytes(srcBuf, TestingCommon.newSeededRandom());
        randomBytes(dstBuf, TestingCommon.newSeededRandom());

        srcDirBuf = ByteBuffers.allocate(size, DIRECT);
        dstDirBuf = ByteBuffers.allocate(size, DIRECT);
        randomBytes(srcDirBuf, TestingCommon.newSeededRandom());
        randomBytes(dstDirBuf, TestingCommon.newSeededRandom());
    }

    private static void randomBytes(ByteBuffer b, Random rand) {

        final int pos = b.position();
        final byte[] array = new byte[b.remaining()];
        rand.nextBytes(array);
        b.put(array);
        b.position(pos);
    }

    @Benchmark
    public void testArrayBytes() {

        srcBuf.rewind();
        dstBuf.rewind();
        final int len = size;
        for (int i = 0; i < len; ++i) {
            dstBuf.put(OctetOps.aPlusB(srcBuf.get(), dstBuf.get(dstBuf.position())));
        }
    }

    @Benchmark
    public void testArrayLongs() {

        srcBuf.rewind();
        dstBuf.rewind();
        final int len = size / SizeOf.LONG;
        for (int i = 0; i < len; ++i) {
            final long eL = UnsignedTypes.readLongUnsignedBytes(srcBuf, SizeOf.LONG);
            dstBuf.putLong(OctetOps.aLongPlusBLong(eL, dstBuf.getLong(dstBuf.position())));
        }
    }

    @Benchmark
    public void testDirectBytes() {

        srcDirBuf.rewind();
        dstDirBuf.rewind();
        final int len = size;
        for (int i = 0; i < len; ++i) {
            dstDirBuf.put(OctetOps.aPlusB(srcDirBuf.get(), dstDirBuf.get(dstDirBuf.position())));
        }
    }

    @Benchmark
    public void testDirectLongs() {

        srcDirBuf.rewind();
        dstDirBuf.rewind();
        final int len = size / SizeOf.LONG;
        for (int i = 0; i < len; ++i) {
            final long eL = UnsignedTypes.readLongUnsignedBytes(srcDirBuf, SizeOf.LONG);
            dstDirBuf.putLong(OctetOps.aLongPlusBLong(eL, dstDirBuf.getLong(dstDirBuf.position())));
        }
    }
}
