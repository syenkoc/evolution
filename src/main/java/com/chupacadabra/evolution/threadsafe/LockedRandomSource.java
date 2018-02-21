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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.chupacadabra.evolution.RandomSource;

/**
 * Locking random source.
 */
final class LockedRandomSource implements RandomSource {

    /**
     * The underlying random source.
     */
    private final RandomSource randomSource;

    /**
     * The lock.
     */
    private final Lock lock;

    /**
     * Constructor.
     * 
     * @param randomSource The underlying random source.
     * @param fair Fairness flag.
     */
    LockedRandomSource(final RandomSource randomSource, final boolean fair) {
        this.randomSource = randomSource;
        this.lock = new ReentrantLock(fair);
    }

    @Override
    public int nextInt(final int n) {
        lock.lock();
        try {
            return randomSource.nextInt(n);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see com.chupacadabra.evolution.RandomSource#nextBoolean()
     */
    @Override
    public boolean nextBoolean() {
        lock.lock();
        try {
            return randomSource.nextBoolean();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see com.chupacadabra.evolution.RandomSource#nextDouble()
     */
    @Override
    public double nextDouble() {
        lock.lock();
        try {
            return randomSource.nextDouble();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see com.chupacadabra.evolution.RandomSource#nextGaussian()
     */
    @Override
    public double nextGaussian() {
        lock.lock();
        try {
            return randomSource.nextGaussian();
        } finally {
            lock.unlock();
        }
    }

}
