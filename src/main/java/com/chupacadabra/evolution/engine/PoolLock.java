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
package com.chupacadabra.evolution.engine;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

import com.chupacadabra.evolution.DifferentialEvolutionSettings;

/**
 * Pool lock.
 */
public interface PoolLock {

    /**
     * Create a no-op lock.
     * 
     * @param settings The settings.
     * @return A no-op pool lock.
     */
    public static PoolLock noOp(final DifferentialEvolutionSettings settings) {
        return new NoOpPoolLock();
    }

    /**
     * Create a reentrant pool lock.
     * 
     * @param settings The settings.
     * @return A suitable pool lock.
     */
    public static PoolLock reentrant(final DifferentialEvolutionSettings settings) {
        boolean fairness = settings.getPoolLockFairness().getReentrantLockValue();
        ReadWriteLock currentPoolLock = new ReentrantReadWriteLock(fairness);
        ReadWriteLock nextPoolLock;

        switch (settings.getPoolReplacement()) {
            case AFTER:
                nextPoolLock = new ReentrantReadWriteLock(fairness);
                break;
            case IMMEDIATELY:
                nextPoolLock = currentPoolLock;
                break;
            default:
                throw new IllegalArgumentException("poolReplacement");
        }

        return new ReadWritePoolLock(currentPoolLock, nextPoolLock);
    }

    /**
     * Create a stamped pool lock.
     * 
     * @param settings The settings.
     * @return A suitable pool lock.
     */
    public static PoolLock stamped(final DifferentialEvolutionSettings settings) {
        ReadWriteLock currentPoolLock = new StampedLock().asReadWriteLock();
        ReadWriteLock nextPoolLock;

        switch (settings.getPoolReplacement()) {
            case AFTER:
                nextPoolLock = new StampedLock().asReadWriteLock();
                break;
            case IMMEDIATELY:
                nextPoolLock = currentPoolLock;
                break;
            default:
                throw new IllegalArgumentException("poolReplacement");
        }

        return new ReadWritePoolLock(currentPoolLock, nextPoolLock);
    }

    /**
     * Acquire the specified lock.
     * 
     * @param poolType The pool type.
     * @param lockType The lock type.
     */
    public void lock(PoolType poolType, LockType lockType);

    /**
     * Release the specified lock.
     * 
     * @param poolType The pool type.
     * @param lockType The lock type.
     */
    public void unlock(PoolType poolType, LockType lockType);

}
