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

import com.chupacadabra.evolution.pool.CandidatePool;

/**
 * The current-to-best differentiation policy.
 * <p>
 * 
 * <p>
 * This policy is typically used in conjunction with the
 * {@linkplain TrialRecombinationPolicy trial recombination policy}.
 */
public final class CurrentToBestDifferentiationPolicy
	implements DifferentiationPolicy
{
	
	/**
	 * Default count.
	 */
	public static final int DEFAULT_COUNT = 1;
			
	/**
	 * The count.
	 */
	private final int count;
	
	/**
	 * Weight for the current.
	 */
	private WeightPolicy currentToRandomWeightPolicy;
	
	/**
	 * Weight for the random candidate.
	 */
	private WeightPolicy randomWeightPolicy;

	/**
	 * @param count
	 * @param currentToRandomWeightPolicy
	 * @param randomWeightPolicy
	 */
	public CurrentToBestDifferentiationPolicy(
			final int count,
			final WeightPolicy currentToRandomWeightPolicy,
			final WeightPolicy randomWeightPolicy)
	{
		this.count = count;
		this.currentToRandomWeightPolicy = currentToRandomWeightPolicy;
		this.randomWeightPolicy = randomWeightPolicy;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentiationPolicy#differentiate(com.chupacadabra.evolution.DifferentialEvolutionState, com.chupacadabra.evolution.RandomSource, int, com.chupacadabra.evolution.pool.CandidatePool)
	 */
	@Override
	public double[] differentiate(
			final DifferentialEvolutionState state,
			final RandomSource randomSource, 
			final int parentIndex, 
			final CandidatePool pool)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
