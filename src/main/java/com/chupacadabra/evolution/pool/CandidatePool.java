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
import com.chupacadabra.evolution.RandomSource;

/**
 * A read-only pool of candidates.
 */
public interface CandidatePool
{
	
	/**
	 * Get the size of this pool.
	 * 
	 * @return The pool size.
	 */
	public int getSize();
	
	/**
	 * Get the index<sup>th</sup> candidate.
	 * 
	 * @param index The index.
	 * @return The specified candidate.
	 */
	public Candidate getCandidate(int index);
	
	/**
	 * Get the index of the best candidate.
	 * 
	 * @return The best candidate index.
	 */
	public int getBestCandidateIndex();
	
	/**
	 * Get the best candidate.
	 * 
	 * @return The best candidate.
	 */
	public default Candidate getBestCandidate()
	{
		return getCandidate(getBestCandidateIndex());
	}
	
	/**
	 * Randomly select <code>count</code> unique candidates, making sure to
	 * exclude the candidates of indices <code>exclude</code>.
	 * 
	 * @param randomSource The source of randomness to use.
	 * @param count The count.
	 * @param exclude The indices to exculde.
	 * @return Randomly selected candidates.
	 */
	public default Candidate[] selectCandidates(final RandomSource randomSource, final int count, final int... exclude) 
	{
		int size = getSize();
		if((count + exclude.length) > size)
		{
			String message = String.format("Cannot select more than %1$s candidates", size);
			throw new IllegalArgumentException(message);
		}
		
		Candidate[] candidates = new Candidate[count];
		int[] selected = new int[count];		
		int selecting = 0;
		
		selecting:
		while(true)
		{
			if(selecting == count)
			{
				break;
			}
			
			int randomIndex = randomSource.nextInt(size);
			for(int index = 0; index < selecting; index++)
			{
				if(randomIndex == selected[index]) 
				{
					// already selected.
					continue selecting;
				}
			}
			
			for(int index = 0; index < exclude.length; index++)
			{
				if(randomIndex == exclude[index]) 
				{
					// specifically excluded.
					continue selecting;
				}
			}
			
			// we found a valid random next. Grab the candidate.
			candidates[selecting] = getCandidate(randomIndex);
			
			// and mark that we selected him.
			selected[selecting] = randomIndex;			
			selecting += 1;
		}
		
		return candidates;
	}
	
}
