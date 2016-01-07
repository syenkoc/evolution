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

import com.chupacadabra.evolution.Candidate;

/**
 * A lockable candidate pool whose locking methods do nothing.
 * <p>
 * This class uses the decorator design pattern.
 */
public class NoOpLockableCandidatePool
	implements LockableCandidatePool
{

	/**
	 * The underlying candidate pool.
	 */
	private final WritableCandidatePool candidatePool;	

	/**
	 * Constructor.
	 * 
	 * @param candidatePool The candidate pool.
	 */
	public NoOpLockableCandidatePool(final WritableCandidatePool candidatePool)
	{
		this.candidatePool = candidatePool;
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getSize()
	 */
	@Override
	public int getSize()
	{
		return candidatePool.getSize();
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getCandidate(int)
	 */
	@Override
	public Candidate getCandidate(final int index)
	{
		return candidatePool.getCandidate(index);
	}

	/**
	 * @see com.chupacadabra.evolution.pool.WritableCandidatePool#setCandidate(int, com.chupacadabra.evolution.Candidate)
	 */
	@Override
	public void setCandidate(final int index, final Candidate candidate)
	{
		candidatePool.setCandidate(index, candidate);
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getBestCandidateIndex()
	 */
	@Override
	public int getBestCandidateIndex()
	{
		return candidatePool.getBestCandidateIndex();
	}

	/**
	 * @see com.chupacadabra.evolution.pool.LockableCandidatePool#readLock()
	 */
	@Override
	public void readLock()
	{
	}

	/**
	 * @see com.chupacadabra.evolution.pool.LockableCandidatePool#readUnlock()
	 */
	@Override
	public void readUnlock()
	{
	}

	/**
	 * @see com.chupacadabra.evolution.pool.LockableCandidatePool#writeLock()
	 */
	@Override
	public void writeLock()
	{
	}

	/**
	 * @see com.chupacadabra.evolution.pool.LockableCandidatePool#writeUnlock()
	 */
	@Override
	public void writeUnlock()
	{
	}

}
