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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.chupacadabra.evolution.util.JavaUtilRandomSource;

/**
 * Settings and policies for the differential evolution optimizer.
 * <p>
 * Using the default constructor of this class will create a reasonable set of
 * default policies suitable for a wide variety of problems. In addition, all of
 * the default policies are safe for use by multiple threads.
 * <p>
 * The setter methods throws {@link NullPointerException NullPointerExceptions} 
 * if the the value is <code>null</code>.
 */
public final class DifferentialEvolutionSettings
	implements Serializable
{

	/**
	 * Default maximum generation: {@value}
	 */
	public static final int DEFAULT_MAXIMUM_GENERATION = 333;

	/**
	 * Default candidate pool size: {@value}
	 */
	public static final int DEFAULT_CANDIDATE_POOL_SIZE = 71;

	/**
	 * Default children per candidate: {@value}
	 */
	public static final int DEFAULT_CHILDREN_PER_CANDIDATE = 1;

	/**
	 * Serial ID. 
	 */
	private static final long serialVersionUID = 4738577104948484276L;
	
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
	 * Pool replacement.
	 */
	private PoolReplacement poolReplacement;

	/**
	 * Exception behavior.
	 */
	private ExceptionBehavior exceptionBehavior;

	/**
	 * User-defined termination criteria.
	 */
	private List<TerminationCriterion> terminationCriteria;

	/**
	 * Constructor.
	 */
	public DifferentialEvolutionSettings()
	{
		maximumGeneration = DEFAULT_MAXIMUM_GENERATION;
		candidatePoolSize = DEFAULT_CANDIDATE_POOL_SIZE;
		childrenPerCandidate = DEFAULT_CHILDREN_PER_CANDIDATE;

		// these policies basically given the "classic" differential evolution
		// algorithm of Storn et al.
		differentiationPolicy = new BestDifferentiationPolicy();
		recombinationPolicy = new BinomialRecombinationPolicy();
		diversityPolicy = new NoDiversityPolicy();
		
		// the only selection policy we know about!
		selectionPolicy = new DebSelectionPolicy();

		// this generally results in faster convergence.
		poolReplacement = PoolReplacement.IMMEDIATELY;

		// any exceptions will terminate the optimization and be throw to the
		// invoker.
		exceptionBehavior = ExceptionBehavior.PROPOGATE;

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
	 * @throws IllegalArgumentException If <code>maximumGeneration</code> is no
	 *             strictly positive.
	 */
	public void setMaximumGeneration(final int maximumGeneration)
	{
		if(maximumGeneration <= 0) 
		{
			throw new IllegalArgumentException("maximumGeneration must be positive");
		}
		
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
	 * @throws IllegalArgumentException If <code>candidatePoolSize</code> is less than 1.
	 */
	public void setCandidatePoolSize(final int candidatePoolSize)
	{
		if(candidatePoolSize < 1) 
		{
			throw new IllegalArgumentException("candidatePoolSize must be greater than 0");
		}
		
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
	 * @throws IllegalArgumentException If <code>childrenPerCandidate</code> is
	 *             less than 1
	 */
	public void setChildrenPerCandidate(final int childrenPerCandidate)
	{
		if(childrenPerCandidate < 1)
		{
			throw new IllegalArgumentException("childrenPerCandidate must be greater than 0");
		}
		
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
	 * @throws NullPointerException If <code>randomSource</code> is <code>null</code>.
	 */
	public void setRandomSource(final RandomSource randomSource)
	{
		if(randomSource == null)
		{
			throw new NullPointerException("randomSource");
		}
		
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
	 * Get the pool replacement type.
	 * 
	 * @return The pool replacement. 
	 */
	public PoolReplacement getPoolReplacement()
	{
		return poolReplacement;
	}
	
	/**
	 * Set the pool replacement type.
	 * 
	 * @param poolReplacement The value.
	 */
	public void setPoolReplacement(final PoolReplacement poolReplacement)
	{
		this.poolReplacement = poolReplacement;
	}
	
	/**
	 * Get the exception behavior.
	 * 
	 * @return The exception behavior.
	 */
	public ExceptionBehavior getExceptionBehavior()
	{
		return exceptionBehavior;
	}
	
	/**
	 * Set the exception behavior.
	 * 
	 * @param exceptionBehavior The value.
	 */
	public void setExceptionBehavior(final ExceptionBehavior exceptionBehavior)
	{
		this.exceptionBehavior = exceptionBehavior;
	}

	/**
	 * Get the termination criteria.
	 * 
	 * @return The termination criteria
	 */
	public List<TerminationCriterion> getTerminationCriteria()
	{
		return terminationCriteria;
	}

	/**
	 * Set the termination criteria.
	 * 
	 * @param terminationCriteria The new criteria.
	 */
	public void setTerminationCriteria(
			final List<TerminationCriterion> terminationCriteria)
	{
		this.terminationCriteria = terminationCriteria;
	}

}
