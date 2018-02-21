/*
 * $Id$
 * 
 * Copyright (c) 2015 Fran Lattanzio
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.chupacadabra.evolution.threadsafe;

import com.chupacadabra.evolution.RandomSource;

/**
 * Synchronized random source.
 */
final class SynchronizedRandomSource implements RandomSource {

    /**
     * Underlying function.
     */
    private final RandomSource randomSource;

    /**
     * The synchronization point.
     */
    private final Object lock;

    /**
     * Constructor.
     * 
     * @param randomSource Underlying source.
     */
    SynchronizedRandomSource(final RandomSource randomSource) {
        this.randomSource = randomSource;
        this.lock = new Object();
    }

    /**
     * @see com.chupacadabra.evolution.RandomSource#nextInt(int)
     */
    @Override
    public int nextInt(final int n) {
        synchronized (lock) {
            return randomSource.nextInt(n);
        }
    }

    /**
     * @see com.chupacadabra.evolution.RandomSource#nextBoolean()
     */
    @Override
    public boolean nextBoolean() {
        synchronized (lock) {
            return randomSource.nextBoolean();
        }
    }

    /**
     * @see com.chupacadabra.evolution.RandomSource#nextDouble()
     */
    @Override
    public double nextDouble() {
        synchronized (lock) {
            return randomSource.nextDouble();
        }
    }

    /**
     * @see com.chupacadabra.evolution.RandomSource#nextGaussian()
     */
    @Override
    public double nextGaussian() {
        synchronized (lock) {
            return randomSource.nextGaussian();
        }
    }

}
