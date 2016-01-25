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


import java.util.concurrent.TimeUnit;

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


@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@Fork(0)
@State(Scope.Benchmark)
public class ArrayAllocationTest {

    @Param({"1", "10", "100", "1000", "10000", "100000", "1000000", "10000000", "100000000"})
    public int size;

    private byte[] preAllocated;


    @Setup
    public void setup() {

        preAllocated = new byte[size];
    }

    @Benchmark
    public byte[] testPreAllocated() {

        final byte[] preAllocated = this.preAllocated;
        for (int i = 0, length = size; i < length; ++i) {
            preAllocated[i] = 0;
        }

        return preAllocated;
    }

    @Benchmark
    public byte[] testNewlyAllocated() {

        return new byte[size];
    }
}
