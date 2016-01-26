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
 * Trial vectors are generated according to:<br>
 * $$\vec{u}_{i} = \vec{x}_{i} + K \cdot (\vec{x}_{r} - \vec{x}_{i}) + F \cdot \sum_{j = 0}^{p}{(\vec{x}_{r^{j}_{2}} - \vec{x}_{r^{j}_{3}})} $$<br>
 * All random candidates are unique, and not equal to the parent.
 * <p>
 * This policy is typically used in conjunction with the
 * {@linkplain TrialRecombinationPolicy trial recombination policy}.
 */
public final class CurrentToBestDifferentiationPolicy
	implements DifferentiationPolicy
{
	
	/**
	 * Default count: {@value}
	 */
	public static final int DEFAULT_COUNT = 1;
			
	/**
	 * Default best weight: {@value}
	 */
	public static final double DEFAULT_RANDOM_WEIGHT = .8d;
	
	/**
	 * Default candidate weight: {@value}
	 */
	public static final double DEFAULT_CANDIDATE_WEIGHT = .5d;
	
	/**
	 * The count.
	 */
	private final int count;
	
	/**
	 * Weight for the best candidate.
	 */
	private WeightPolicy currentToBestWeightPolicy;
	
	/**
	 * Weight policy for the random candidates.
	 */
	private WeightPolicy randomWeightPolicy;
	
	/**
	 * Constructor.
	 */
	public CurrentToBestDifferentiationPolicy()
	{
		this(DEFAULT_COUNT, new FixedWeightPolicy(DEFAULT_RANDOM_WEIGHT), new FixedWeightPolicy(DEFAULT_CANDIDATE_WEIGHT));
	}

	/**
	 * Constructor.
	 * 
	 * @param count The candidate count.
	 * @param currentToBestWeightPolicy Current-to-best weight.
	 * @param randomWeightPolicy Random candidate
	 */
	public CurrentToBestDifferentiationPolicy(
			final int count,
			final WeightPolicy currentToBestWeightPolicy,
			final WeightPolicy randomWeightPolicy)
	{
		this.count = count;
		this.currentToBestWeightPolicy = currentToBestWeightPolicy;
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
		// grab the candidates.
		int bestIndex = pool.getBestCandidateIndex();
		Candidate parent = pool.getCandidate(parentIndex);
		Candidate bestCandidate = pool.getBestCandidate();
		
		// generate weights.
		double k = currentToBestWeightPolicy.getWeight(state, randomSource);
		double f = randomWeightPolicy.getWeight(state, randomSource);
		
		// assemble child vector.
		double[] child = parent.getParameters();
		
		// first, do best differentiation.
		PairwiseWeightedParameterSum.computeInPlace(f, child, 0, bestCandidate, parent);
		
		// now, select random candidates.
		int randomCount = count * 2;
		Candidate[] random = pool.selectCandidates(randomSource, randomCount, parentIndex, bestIndex);
		
		// and apply for differentiation for random candidates.
		PairwiseWeightedParameterSum.computeInPlace(k, child, 0, random);
	
		return child;
	}

}
