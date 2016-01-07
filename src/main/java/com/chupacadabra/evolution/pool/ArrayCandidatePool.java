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
 * Array-based candidate pool.
 */
public final class ArrayCandidatePool
	implements WritableCandidatePool
{

	/**
	 * Sentinel value to indicate no best candidate index has been set.
	 */
	private static final int NO_BEST_CANDIDATE_INDEX = -1;

	/**
	 * The pool.
	 */
	private final Candidate[] pool;

	/**
	 * The best index.
	 */
	private int bestCandidateIndex;

	/**
	 * Constructor.
	 * 
	 * @param size The size.
	 */
	public ArrayCandidatePool(final int size)
	{
		pool = new Candidate[size];
		bestCandidateIndex = NO_BEST_CANDIDATE_INDEX;
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getSize()
	 */
	@Override
	public int getSize()
	{
		return pool.length;
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getCandidate(int)
	 */
	@Override
	public Candidate getCandidate(final int index)
	{
		return pool[index];
	}

	/**
	 * @see com.chupacadabra.evolution.pool.WritableCandidatePool#setCandidate(int, com.chupacadabra.evolution.Candidate)
	 */
	@Override
	public void setCandidate(final int index, final Candidate candidate)
	{
		// store the candidate.
		pool[index] = candidate;

		// see if this candidate is the best one yet.

		if(candidate.isFeasible() == false)
		{
			// the best candidate must of course be feasible!
			return;
		}

		if((bestCandidateIndex == -1) || 
				(candidate.getFitness() < pool[bestCandidateIndex].getFitness()))
		{
			// we found a new best candidate
			bestCandidateIndex = index;
		}
	}

	/**
	 * @see com.chupacadabra.evolution.pool.CandidatePool#getBestCandidateIndex()
	 */
	@Override
	public int getBestCandidateIndex()
	{
		return bestCandidateIndex;
	}

}
