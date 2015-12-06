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
package com.chupacadabra.evolution.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.DifferentialEvolutionResult;
import com.chupacadabra.evolution.DifferentialEvolutionState;
import com.chupacadabra.evolution.ExceptionEncountered;
import com.chupacadabra.evolution.MaximumGenerationReached;
import com.chupacadabra.evolution.TerminationCriterion;
import com.chupacadabra.evolution.TerminationCriterionMet;
import com.chupacadabra.evolution.TerminationReason;
import com.chupacadabra.evolution.pool.ArrayCandidatePool;
import com.chupacadabra.evolution.pool.CandidatePool;
import com.chupacadabra.evolution.pool.ReadWriteLockCandidatePool;
import com.chupacadabra.evolution.util.TimeLength;

/**
 * Core fork/join differential evolution optimizer implementation.
 * <p>
 * Instances of this class are <i>not</i> safe for use by multiple threads.
 */
final class ForkJoinDifferentialEvolutionOptimizerCore implements DifferentialEvolutionState
{
	
	// invariant.

	/**
	 * The fork-join pool to use.
	 */
	private final ForkJoinPool forkJoinPool;

	/**
	 * The problem.
	 */
	private final DifferentialEvolutionProblem problem;

	/**
	 * The policies.
	 */
	private final DifferentialEvolutionSettings settings;

	/**
	 * The start time in nanoseconds.
	 */
	private volatile long startTimeInNanos;
	
	/**
	 * The current generation.
	 */
	private volatile int currentGeneration;

	/**
	 * The current pool of candidates.
	 */
	private volatile CandidatePool currentPool;

	/**
	 * Constructor.
	 * 
	 * @param forkJoinPool The pool to use.
	 * @param problem The problem.
	 * @param settings The settings.
	 */
	ForkJoinDifferentialEvolutionOptimizerCore(
			final ForkJoinPool forkJoinPool, 
			final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings settings)
	{
		this.forkJoinPool = forkJoinPool;
		this.problem = problem;
		this.settings = settings;
	}
	
	/**
	 * Perform optimization.
	 * 
	 * @return The results.
	 */
	public DifferentialEvolutionResult optimize() 
	{
		try
		{
			startTimeInNanos = System.nanoTime();
			
			// we consider initializing the pool to be a generation.
			currentGeneration = 1;
			initializePool();
			
			// run each generation.
			int maximumGeneration = settings.getMaximumGeneration();
			
			while(true)
			{
				// fixed termination criterion.
				if(currentGeneration >= maximumGeneration)
				{
					return result(new MaximumGenerationReached(currentGeneration));
				}
				
				// check user-defined termination criteria.
				for(TerminationCriterion criterion : settings.getTerminationCriteria()) 
				{
					if(criterion.isMet(this))
					{
						return result(new TerminationCriterionMet(criterion));
					}
				}
				
				// do one generation.
				iterate();
				currentGeneration += 1;
			}
		}
		catch(final RuntimeException re)
		{
			switch(settings.getExceptionBehavior())
			{
				case TERMINATE:
					// prepare a solution.
					return result(new ExceptionEncountered(re));
				case PROPOGATE:
				default:
					// retoss.
					throw re;
			}
		}
	}
	
	/**
	 * Create a result with the specified termination reason.
	 * 
	 * @param terminationReason The termination reason.
	 * @return A suitable result.
	 */
	private DifferentialEvolutionResult result(final TerminationReason terminationReason)
	{
		Candidate bestCandidate = getBestCandidate();
		TimeLength timeTaken = getTimeTaken();
				
		// assemble the result.
		DifferentialEvolutionResult result = new DifferentialEvolutionResult(bestCandidate, terminationReason, timeTaken);
		
		return result;		
	}
	
	/**
	 * Create a suitable candidate pool.
	 * 
	 * @return The candidate pool.
	 */
	private CandidatePool createCandidatePool() 
	{
		// first create an array pool.
		int poolSize = settings.getCandidatePoolSize();
		ArrayCandidatePool arrayPool = new ArrayCandidatePool(poolSize);
		
		// and wrap up in a threadsafe decorator.
		ReadWriteLockCandidatePool lockingPool = new ReadWriteLockCandidatePool(arrayPool);
		
		return lockingPool;		
	}
	
	/**
	 * Initialize the current pool.
	 */
	private void initializePool()
	{
		int candidateCount = settings.getCandidatePoolSize();
		
		// make a new pool.
		currentPool = createCandidatePool();
		
		// spawn a task to generate a candidate for each index.
		List<ForkJoinTask<Void>> tasks = new ArrayList<ForkJoinTask<Void>>(candidateCount);		
		for(int index = 0; index < candidateCount; index++)
		{
			GenerateRandomFeasibleCandidateTask task = new GenerateRandomFeasibleCandidateTask(problem, settings, index, currentPool);
			tasks.add(forkJoinPool.submit(task));
		}
		
		// wait for all initialization tasks to finish.
		for(int index = 0; index < candidateCount; index++)
		{
			tasks.get(index).join();
		}		
	}
	
	/**
	 * Iterate, <i>i.e.</i> perform one iteration.
	 */
	private void iterate()
	{
		// potentially create a new pool.
		CandidatePool nextPool = getNextCandidatePool();
		
		int poolSize = settings.getCandidatePoolSize();
		List<ForkJoinTask<Void>> tasks = new ArrayList<ForkJoinTask<Void>>(poolSize);
				
		// spawn a task for each index...
		for(int index = 0; index < poolSize; index++)
		{
			CandidateIndexAction task = new CandidateIndexAction(problem, settings, this, index, currentPool, nextPool);			
			tasks.add(forkJoinPool.submit(task));
		}
		
		// ... now just wait for all tasks to finish.
		for(int index = 0; index < poolSize; index++)
		{
			tasks.get(index).join();
		}
				
		// and we have a new pool.
		currentPool = nextPool;
	}

	/**
	 * Get the next candidate pool.
	 * 
	 * @return The next candidate pool.
	 */
	private CandidatePool getNextCandidatePool()
	{
		switch(settings.getPoolReplacement())
		{
			case AFTER:
				CandidatePool newPool = createCandidatePool();
				
				return newPool;
			case IMMEDIATELY:
				// ha, we just replace in the current pool!
				return currentPool;
			default:
				throw new InternalError();
		}
	}
	
	// state implementation.

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionState#getDimension()
	 */
	@Override
	public int getDimension()
	{
		return problem.getDimension();
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionState#getBestCandidate()
	 */
	@Override
	public Candidate getBestCandidate()
	{
		return currentPool.getBestCandidate();
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionState#getGeneration()
	 */
	@Override
	public int getGeneration()
	{
		return currentGeneration;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionState#getMaximumGeneration()
	 */
	@Override
	public int getMaximumGeneration()
	{
		return settings.getMaximumGeneration();
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionState#getTimeTaken()
	 */
	@Override
	public TimeLength getTimeTaken()
	{
		long nanos = System.nanoTime() - startTimeInNanos;
		TimeLength timeTaken = new TimeLength(nanos, TimeUnit.NANOSECONDS);
		
		return timeTaken;
	}
	
}
