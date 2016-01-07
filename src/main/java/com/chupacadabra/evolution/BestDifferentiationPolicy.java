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
 * The random differentiation policy. 
 * <p>
 * Trial candidates of the i<sup>th</sup> parent vector are 
 * constructed according to the formula:<br>
 * $$\vec{u}_{i} = \vec{x}_{best} + F \cdot \sum_{j = 0}^{p}{(\vec{x}_{r^{j}_{2}} - \vec{x}_{r^{j}_{3}})}$$
 * <br>
 * Here, <code>p &gt; 0</code> is the vector count and <code>F</code> is the 
 * weight. The randomly chosen candidate vectors are neither the parent nor 
 * the best candidate.
 * <p>
 * By default, we choose <code>p = 1</code> and use a 
 * {@linkplain DitheringWeightPolicy dithering weight policy}. 
 */
public final class BestDifferentiationPolicy 
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
	 * The weight policy.
	 */
	private final WeightPolicy weightPolicy;

	/**
	 * Default constructor.
	 * <p>
	 * This constructor uses:
	 * <ul>
	 *  <li>{@linkplain #DEFAULT_COUNT The default count.}</li>
	 *  <li>{@linkplain DitheringWeightPolicy The default dithering weight policy.}</li>
	 * </ul>
	 */
	public BestDifferentiationPolicy()
	{
		this(DEFAULT_COUNT, new DitheringWeightPolicy());
	}
	
	/**
	 * Constructor.
	 * 
	 * @param count The count.
	 * @param weightPolicy The weight policy.
	 */
	public BestDifferentiationPolicy(final int count, final WeightPolicy weightPolicy)
	{
		this.count = count;
		this.weightPolicy = weightPolicy;
	}
	
	/**
	 * @see com.chupacadabra.evolution.DifferentiationPolicy#differentiate(com.chupacadabra.evolution.DifferentialEvolutionState, RandomSource, int, com.chupacadabra.evolution.pool.CandidatePool)
	 */
	@Override
	public double[] differentiate(
			final DifferentialEvolutionState state, 
			final RandomSource randomSource,
			final int parentIndex, 
			final CandidatePool pool)
	{		
		// the trial vector starts at the current best candidate.
		int bestIndex = pool.getBestCandidateIndex();
		Candidate best = pool.getCandidate(bestIndex);
		double[] trial = best.getParameters();

		// select the desired number of random candidates, excluding the parent and best candidate.
		int total = 2 * count;
		Candidate[] candidates = pool.selectCandidates(randomSource, total, parentIndex, bestIndex);
				
		// grab a weight.
		double f = weightPolicy.getWeight(state, randomSource);		
		
		// sum up the parameters.
		PairwiseWeightedParameterSum.computeInPlace(f, trial, 0, candidates);
				
		return trial;
	}

}
