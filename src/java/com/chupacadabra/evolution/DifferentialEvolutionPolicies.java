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

import java.util.ArrayList;
import java.util.List;

import com.chupacadabra.evolution.util.JavaUtilRandomSource;

/**
 * Policies for the differential evolution optimizer.
 * <p>
 * Using the default constructor of this class will create a reasonable set of
 * default policies suitable for a wide variety of problems. In addition, all of
 * the default policies are safe for use by multiple threads.
 */
public final class DifferentialEvolutionPolicies
{

	/**
	 * Default maximum generation: {@value}
	 */
	public static final int DEFAULT_MAXIMUM_GENERATION = 3333;

	/**
	 * Default candidate pool size: {@value}
	 */
	public static final int DEFAULT_CANDIDATE_POOL_SIZE = 50;

	/**
	 * Default children per candidate: {@value}
	 */
	public static final int DEFAULT_CHILDREN_PER_CANDIDATE = 5;

	/**
	 * Maximum generation.
	 */
	private int maximumGeneration;

	/**
	 * The pool size.
	 */
	private int candidatePoolSize;

	/**
	 * Children to generate per candidate.
	 */
	private int childrenPerCandidate;

	/**
	 * Source of randomness.
	 */
	private RandomSource randomSource;

	/**
	 * Differentiation policy.
	 */
	private DifferentiationPolicy differentiationPolicy;

	/**
	 * Recombination policy.
	 */
	private RecombinationPolicy recombinationPolicy;

	/**
	 * Candidate selection policy.
	 */
	private SelectionPolicy selectionPolicy;
	
	/**
	 * Diversity policy.
	 */
	private DiversityPolicy diversityPolicy;

	/**
	 * Pool replacement policy.
	 */
	private PoolReplacementPolicy poolReplacementPolicy;

	/**
	 * Exception handling policy.
	 */
	private ExceptionHandlingPolicy exceptionHandlingPolicy;

	/**
	 * User-defined termination criteria.
	 */
	private List<TerminationCriterion> terminationCriteria;

	/**
	 * Constructor.
	 */
	public DifferentialEvolutionPolicies()
	{
		maximumGeneration = DEFAULT_MAXIMUM_GENERATION;
		candidatePoolSize = DEFAULT_CANDIDATE_POOL_SIZE;
		childrenPerCandidate = DEFAULT_CHILDREN_PER_CANDIDATE;

		// these policies basically given the "classic" differential evolution
		// algorithm of Storn et al.
		differentiationPolicy = new BestDifferentiationPolicy();
		recombinationPolicy = new BinomialRecombinationPolicy();
		diversityPolicy = new NoDiversityPolicy();

		// replace candidates immediately.
		poolReplacementPolicy = PoolReplacementPolicy.IMMEDIATELY;

		// any exceptions will terminate the optimization and be throw to the
		// invoker.
		exceptionHandlingPolicy = ExceptionHandlingPolicy.PROPOGATE;

		// use the only implementation we know of.
		randomSource = new JavaUtilRandomSource();

		// empty!
		terminationCriteria = new ArrayList<TerminationCriterion>();
	}

	/**
	 * Get the maximum generation.
	 * 
	 * @return The maximum generation.
	 */
	public int getMaximumGeneration()
	{
		return maximumGeneration;
	}

	/**
	 * Set the maximum generation.
	 * 
	 * @param maximumGeneration The value.
	 */
	public void setMaximumGeneration(final int maximumGeneration)
	{
		this.maximumGeneration = maximumGeneration;
	}

	/**
	 * Get the candidate pool size.
	 * 
	 * @return The pool size.
	 */
	public int getCandidatePoolSize()
	{
		return candidatePoolSize;
	}

	/**
	 * Set the candidate pool size.
	 * 
	 * @param candidatePoolSize The new pool size.
	 */
	public void setCandidatePoolSize(final int candidatePoolSize)
	{
		this.candidatePoolSize = candidatePoolSize;
	}

	/**
	 * Get the number of child to generate per candidate during each generation.
	 * 
	 * @return The number of children.
	 */
	public int getChildrenPerCandidate()
	{
		return childrenPerCandidate;
	}

	/**
	 * Set the number of children to generate per candidate.
	 * 
	 * @param childrenPerCandidate The new number of children.
	 */
	public void setChildrenPerCandidate(final int childrenPerCandidate)
	{
		this.childrenPerCandidate = childrenPerCandidate;
	}

	/**
	 * Get the source of randomness to use.
	 * 
	 * @return The source of randomness.
	 */
	public RandomSource getRandomSource()
	{
		return randomSource;
	}

	/**
	 * Set the source of randomness.
	 * 
	 * @param randomSource The new source.
	 */
	public void setRandomSource(final RandomSource randomSource)
	{
		this.randomSource = randomSource;
	}

	/**
	 * Get the differentiation policy.
	 * 
	 * @return The differentiation policy.
	 */
	public DifferentiationPolicy getDifferentiationPolicy()
	{
		return differentiationPolicy;
	}

	/**
	 * Set the differentiation policy.
	 * 
	 * @param differentiationPolicy The new differentiation policy.
	 */
	public void setDifferentiationPolicy(
			final DifferentiationPolicy differentiationPolicy)
	{
		this.differentiationPolicy = differentiationPolicy;
	}

	/**
	 * Get the recombination policy.
	 * 
	 * @return The recombinationPolicy The recombination policy.
	 */
	public RecombinationPolicy getRecombinationPolicy()
	{
		return recombinationPolicy;
	}
	
	/**
	 * Set the recombination policy.
	 * 
	 * @param recombinationPolicy The new recombination policy.
	 */
	public void setRecombinationPolicy(final RecombinationPolicy recombinationPolicy)
	{
		this.recombinationPolicy = recombinationPolicy;
	}

	/**
	 * Get the diversity policy.
	 * 
	 * @return The diversity policy.
	 */
	public DiversityPolicy getDiversityPolicy()
	{
		return diversityPolicy;
	}

	/**
	 * Set the diversity policy.
	 * 
	 * @param diversityPolicy The new diversity policy.
	 */
	public void setDiversityPolicy(final DiversityPolicy diversityPolicy)
	{
		this.diversityPolicy = diversityPolicy;
	}

	/**
	 * Get the exception handling policy.
	 * 
	 * @return The exception handling policy.
	 */
	public ExceptionHandlingPolicy getExceptionHandlingPolicy()
	{
		return exceptionHandlingPolicy;
	}

	/**
	 * Set the exception handling policy.
	 * 
	 * @param exceptionHandlingPolicy The new exception handling policy.
	 */
	public void setExceptionHandlingPolicy(
			final ExceptionHandlingPolicy exceptionHandlingPolicy)
	{
		this.exceptionHandlingPolicy = exceptionHandlingPolicy;
	}

	/**
	 * Set the selection policy.
	 * 
	 * @return The selection policy.
	 */
	public SelectionPolicy getSelectionPolicy()
	{
		return selectionPolicy;
	}

	/**
	 * Set the selection policy.
	 * 
	 * @param selectionPolicy The new selection policy.
	 */
	public void setSelectionPolicy(final SelectionPolicy selectionPolicy)
	{
		this.selectionPolicy = selectionPolicy;
	}

	/**
	 * @return The poolReplacementPolicy
	 */
	public PoolReplacementPolicy getPoolReplacementPolicy()
	{
		return poolReplacementPolicy;
	}

	/**
	 * @param poolReplacementPolicy The value.
	 */
	public void setPoolReplacementPolicy(
			PoolReplacementPolicy poolReplacementPolicy)
	{
		this.poolReplacementPolicy = poolReplacementPolicy;
	}

	/**
	 * @return The terminationCriteria
	 */
	public List<TerminationCriterion> getTerminationCriteria()
	{
		return terminationCriteria;
	}

	/**
	 * @param terminationCriteria The value.
	 */
	public void setTerminationCriteria(
			final List<TerminationCriterion> terminationCriteria)
	{
		this.terminationCriteria = terminationCriteria;
	}

}
