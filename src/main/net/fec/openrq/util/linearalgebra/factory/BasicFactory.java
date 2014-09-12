/*
 * Copyright 2014 Jose Lopes
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

/*
 * Copyright 2011-2014, by Vladimir Kostyukov and Contributors.
 * 
 * This file is part of la4j project (http://la4j.org)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributor(s): -
 */
package net.fec.openrq.util.linearalgebra.factory;


import java.util.Arrays;
import java.util.Random;

import net.fec.openrq.util.linearalgebra.vector.ByteVector;
import net.fec.openrq.util.linearalgebra.vector.dense.BasicByteVector;
import net.fec.openrq.util.linearalgebra.vector.source.VectorSource;


public abstract class BasicFactory extends Factory {

    private static final long serialVersionUID = 4071505L;


    @Override
    public ByteVector createVector() {

        return new BasicByteVector();
    }

    @Override
    public ByteVector createVector(int length) {

        return new BasicByteVector(length);
    }

    @Override
    public ByteVector createVector(byte[] array) {

        return new BasicByteVector(array);
    }

    @Override
    public ByteVector createVector(ByteVector vector) {

        return new BasicByteVector(vector);
    }

    @Override
    public ByteVector createVector(VectorSource source) {

        return new BasicByteVector(source);
    }

    @Override
    public ByteVector createConstantVector(int length, byte value) {

        byte array[] = new byte[length];
        Arrays.fill(array, value);

        return new BasicByteVector(array);
    }

    @Override
    public ByteVector createRandomVector(int length, Random random) {

        byte array[] = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = (byte)random.nextInt();
        }

        return new BasicByteVector(array);
    }
}