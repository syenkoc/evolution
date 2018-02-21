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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Reentrant pool lock.
 */
final class ReadWritePoolLock implements PoolLock {

    /**
     * The current pool lock.
     */
    private final ReadWriteLock currentPoolLock;

    /**
     * The next pool lock.
     */
    private final ReadWriteLock nextPoolLock;

    /**
     * Constructor.
     * 
     * @param currentPoolLock Current pool lock.
     * @param nextPoolLock Next pool lock.
     */
    ReadWritePoolLock(final ReadWriteLock currentPoolLock, final ReadWriteLock nextPoolLock) {
        super();
        this.currentPoolLock = currentPoolLock;
        this.nextPoolLock = nextPoolLock;
    }

    /**
     * @see com.chupacadabra.evolution.engine.PoolLock#lock(com.chupacadabra.evolution.engine.PoolType,
     *      com.chupacadabra.evolution.engine.LockType)
     */
    @Override
    public void lock(final PoolType poolType, final LockType lockType) {
        Lock lock = getLock(poolType, lockType);
        lock.lock();
    }

    /**
     * @see com.chupacadabra.evolution.engine.PoolLock#unlock(com.chupacadabra.evolution.engine.PoolType,
     *      com.chupacadabra.evolution.engine.LockType)
     */
    @Override
    public void unlock(final PoolType poolType, final LockType lockType) {
        Lock lock = getLock(poolType, lockType);
        lock.unlock();
    }

    /**
     * Get the desired lock.
     * 
     * @param poolType The pool type.
     * @param lockType The lock type.
     * @return The lock.
     */
    private Lock getLock(final PoolType poolType, final LockType lockType) {
        ReadWriteLock readWriteLock = getLock(poolType);
        switch (lockType) {
            case READ:
                return readWriteLock.readLock();
            case WRITE:
                return readWriteLock.writeLock();
            default:
                throw new IllegalArgumentException("lockType");
        }
    }

    /**
     * Get the lock for the specified pool type.
     * 
     * @param poolType The pool type.
     * @return The desired lock.
     */
    private ReadWriteLock getLock(final PoolType poolType) {
        switch (poolType) {
            case CURRENT:
                return currentPoolLock;
            case NEXT:
                return nextPoolLock;
            default:
                throw new IllegalArgumentException("poolType");
        }
    }

}
