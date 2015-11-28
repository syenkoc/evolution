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
package com.chupacadabra.evolution.pool;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.RandomSource;


/**
 * Uses a {@link ReadWriteLock} to ensure theadsafety of an underlying pool.
 * <p>
 * This class uses the decorator design pattern.
 */
public final class ReadWriteLockCandidatePool implements CandidatePool
{
	
	/**
	 * Underlying pool.
	 */
	private final CandidatePool candidatePool;
	
	/**
	 * The lock.
	 */
	private final ReadWriteLock readWriteLock;

	/**
	 * Constructor.
	 * 
	 * @param candidatePool Candidate pool to wrap.
	 */
	public ReadWriteLockCandidatePool(final CandidatePool candidatePool)
	{
		this.candidatePool = candidatePool;
		this.readWriteLock = new ReentrantReadWriteLock();
	}

	@Override
	public int getSize()
	{
		readWriteLock.readLock().lock();
		try
		{
			return candidatePool.getSize();
		} 
		finally
		{
			readWriteLock.readLock().unlock();
		}
	}

	@Override
	public Candidate getCandidate(final int index)
	{
		readWriteLock.readLock().lock();
		try
		{
			return candidatePool.getCandidate(index);
		}
		finally
		{
			readWriteLock.readLock().unlock();
		}
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getBestCandidate()
	 */
	@Override
	public Candidate getBestCandidate()
	{
		readWriteLock.readLock().lock();
		try
		{
			return candidatePool.getBestCandidate();
		}
		finally 
		{
			readWriteLock.readLock().unlock();
		}
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#selectCandidates(com.chupacadabra.evolution.RandomSource, int, int[])
	 */
	@Override
	public Candidate[] selectCandidates(
			final RandomSource randomSource,
			final int count, 
			final int... exclude)
	{
		readWriteLock.readLock().lock();
		try
		{
			return candidatePool.selectCandidates(randomSource, count, exclude);
		}
		finally 
		{
			readWriteLock.readLock().unlock();
		}
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#setCandidate(int, com.chupacadabra.evolution.Candidate)
	 */
	@Override
	public void setCandidate(final int index, final Candidate candidate)
	{
		readWriteLock.writeLock().lock();
		try
		{
			candidatePool.setCandidate(index, candidate);
		}
		finally
		{
			readWriteLock.writeLock().unlock();
		}
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getBestCandidateIndex()
	 */
	@Override
	public int getBestCandidateIndex()
	{
		readWriteLock.readLock().lock();
		try
		{
			return candidatePool.getBestCandidateIndex();
		}
		finally
		{
			readWriteLock.readLock().unlock();
		}
	}
	
}
