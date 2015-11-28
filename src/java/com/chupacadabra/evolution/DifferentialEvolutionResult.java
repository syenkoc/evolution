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
package com.chupacadabra.evolution;

import com.chupacadabra.evolution.util.TimeLength;

/**
 * Result of a differential evolution.
 */
public final class DifferentialEvolutionResult
{

	/**
	 * The best candidate.
	 */
	private final Candidate bestCandidate;

	/**
	 * Termination reason.
	 */
	private final TerminationReason terminationReason;

	/**
	 * Time taken.
	 */
	private final TimeLength timeTaken;

	/**
	 * Constructor.
	 * 
	 * @param bestCandidate The best candidate.
	 * @param terminationReason The reason for termination.
	 * @param timeTaken The time taken.
	 */
	public DifferentialEvolutionResult(final Candidate bestCandidate,
			final TerminationReason terminationReason,
			final TimeLength timeTaken)
	{
		this.bestCandidate = bestCandidate;
		this.terminationReason = terminationReason;
		this.timeTaken = timeTaken;
	}
	
	/**
	 * Get the best candidate.
	 * 
	 * @return The best candidate.
	 */
	public Candidate getBestCandidate()
	{
		return bestCandidate;
	}
	
	/**
	 * Get the termination reason.
	 * 
	 * @return The termination reason.
	 */
	public TerminationReason getTerminationReason()
	{
		return terminationReason;
	}
	
	/**
	 * Get the time taken.
	 * 
	 * @return The time taken.
	 */
	public TimeLength getTimeTaken()
	{
		return timeTaken;
	}
	
}
