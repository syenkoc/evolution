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

import java.util.Arrays;

import com.chupacadabra.evolution.pool.CandidatePool;
import com.chupacadabra.evolution.util.CandidateFitnessComparator;

/**
 * The directional differentiation policy.
 * <p>
 * Trial vectors 
 * <p>
 * This policy is typically used in conjunction with the
 * {@linkplain TrialRecombinationPolicy trial recombination policy}.
 */
public final class DirectionalDifferentiationPolicy
	implements DifferentiationPolicy
{
	
	/**
	 * Default number of candidate pairs to use to generate a direction: {@value}
	 */
	public static final int DEFAULT_COUNT = 2;
	
	/**
	 * The default fixed weight: {@value}
	 */
	public static final double DEFAULT_FIXED_WEIGHT = 0.5d;
	
	/**
	 * The number of candidate vector <i>pairs</i> to pull.
	 */
	private final int count;
	
	/**
	 * Weight policy.
	 */
	private final WeightPolicy weightPolicy;
	
	/**
	 * Default constructor.
	 * <p>
	 * Uses a {@linkplain #DEFAULT_FIXED_WEIGHT fixed weight policy}.
	 */
	public DirectionalDifferentiationPolicy()
	{
		this(DEFAULT_COUNT, new FixedWeightPolicy(DEFAULT_FIXED_WEIGHT));
	}

	/**
	 * Constructor.
	 * 
	 * @param count The count.
	 * @param weightPolicy The weight policy.
	 */
	public DirectionalDifferentiationPolicy(final int count, final WeightPolicy weightPolicy)
	{
		this.count = count;
		this.weightPolicy = weightPolicy;
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
		// grab the desired number of random candidates, making sure exclude the parent.
		int total = count * 2;
		Candidate[] randomCandidates = pool.selectCandidates(randomSource, total, parentIndex);
		
		// sort candidates based on fitness.
		Arrays.sort(randomCandidates, new CandidateFitnessComparator());
		
		// the trial vector starts out as the best of the randomly selected
		// candidate vectors.
		double[] trial = randomCandidates[0].getParameters().clone();

		// grab weight and normalized based on pair count.
		double f = weightPolicy.getWeight(state, randomSource) / (double)count;

		// do pairwise summing.
		PairwiseWeightedParameterSum.computeInPlace(f, trial, 0, randomCandidates);
		
		return trial;
	}

}
