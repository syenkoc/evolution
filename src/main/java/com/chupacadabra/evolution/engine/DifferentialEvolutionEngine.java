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
package com.chupacadabra.evolution.engine;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.DifferentialEvolutionResult;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.ExceptionEncountered;
import com.chupacadabra.evolution.MaximumGenerationReached;
import com.chupacadabra.evolution.SimpleDifferentialEvolutionResult;
import com.chupacadabra.evolution.TerminationCriterion;
import com.chupacadabra.evolution.TerminationCriterionMet;
import com.chupacadabra.evolution.TerminationReason;
import com.chupacadabra.evolution.pool.LockableCandidatePool;
import com.chupacadabra.evolution.util.TimeLength;

/**
 * The core differential evolution optimization engine.
 * <p>
 * Basically this class strings together the pool creation, pool initialization,
 * and generation iteration policies to create a fully-fledged optimizer.
 */
public final class DifferentialEvolutionEngine
	implements DifferentialEvolutionReceiver
{

	// strategies.
	
	/**
	 * Pool creation strategy.
	 */
	private final PoolCreation poolCreation;

	/**
	 * Initialization strategy.
	 */
	private final Initialization initialization;
	
	/**
	 * Iteration strategy.
	 */
	private final Iteration iteration;
	
	/**
	 * Child generation strategy.
	 */
	private final ChildGeneration childGeneration;
	
	/**
	 * The problem.
	 */
	private DifferentialEvolutionProblem problem;
	
	/**
	 * The settings.
	 */
	private DifferentialEvolutionSettings settings;
	
	// updated.
	
	/**
	 * The time at which we started optimizing, in nanoseconds.
	 */
	private volatile long startTimeInNanos;
	
	/**
	 * The current generation.
	 */
	private volatile int currentGeneration;
	
	/**
	 * The current pool of candidates.
	 */
	private volatile LockableCandidatePool currentPool;
	
	/**
	 * The next pool of candidates.
	 * <p>
	 * Could be the same as the current pool depending on the replacement policy.
	 */
	private volatile LockableCandidatePool nextPool;
		
	/**
	 * Constructor.
	 * 
	 * @param poolCreation Pool creation strategy.
	 * @param initialization Initialization strategy.
	 * @param iteration Iteration strategy.
	 * @param childGeneration Child generation strategy.
	 */
	public DifferentialEvolutionEngine(
			final PoolCreation poolCreation,
			final Initialization initialization,
			final Iteration iteration,
			final ChildGeneration childGeneration)
	{
		// store the strategy magic.
		this.poolCreation = poolCreation;
		this.initialization = initialization;
		this.iteration = iteration;
		this.childGeneration = childGeneration;
	}
	
	/**
	 * Get the result for the specified problem, using the settings.
	 * 
	 * @param problem The problem.
	 * @param settings The settings.
	 * @return The result.
	 */
	public DifferentialEvolutionResult getResult(final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings settings)
	{	
		this.problem = problem;
		this.settings = settings;
		
		try
		{
			DifferentialEvolutionResult result = optimizeCore();
			
			return result;
		}
		catch(final Exception re)
		{
			// do dance based on user-specified exception policy.
			switch(settings.getExceptionBehavior())
			{
				case TERMINATE:
					return createResult(new ExceptionEncountered(re));
				case PROPOGATE:
				default:
					if(re instanceof RuntimeException)
					{
						throw (RuntimeException)re;
					}
					
					throw new RuntimeException(re);
			}			
		}	
	}
	
	/**
	 * Core optimization method.
	 * 
	 * @return The result.
	 * @throws InterruptedException If the invoking thread is interrupted.
	 * @throws ExecutionException If a sub-task aborts.
	 */
	private DifferentialEvolutionResult optimizeCore()
		throws InterruptedException, ExecutionException
	{
		// and... we're off!
		startTimeInNanos = System.nanoTime();
		currentGeneration = 0;

		// initialize the current pool.
		currentPool = poolCreation.create(this);
		initialization.initialize(this);
		currentGeneration += 1;

		while(true)
		{
			// check for termination (convergence, etc.).
			for(TerminationCriterion criterion : problem
					.getTerminationCriteria())
			{
				if(criterion.isMet(this))
				{
					return createResult(new TerminationCriterionMet(criterion));
				}
			}

			if(currentGeneration >= getMaximumGeneration())
			{
				return createResult(new MaximumGenerationReached(
						getMaximumGeneration()));
			}

			// perform one iteration.
			nextPool = createNextPool();
			iteration.iterate(this, childGeneration);
			currentPool = nextPool;
			currentGeneration += 1;
		}
	}
	
	/**
	 * Create a result with the specified termination reason and current best candidate.
	 * 
	 * @param terminationReason The termination reason.
	 * @return A result.
	 */
	private DifferentialEvolutionResult createResult(final TerminationReason terminationReason)
	{
		Candidate bestCandidate = getBestCandidate();
		TimeLength timeTaken = getTimeTaken();
		
		SimpleDifferentialEvolutionResult result = new SimpleDifferentialEvolutionResult();
		result.setBestCandidate(bestCandidate);
		result.setTerminationReason(terminationReason);
		result.setTimeTaken(timeTaken);
		
		return result;		
	}
					
	/**
	 * Create the next pool.
	 * 
	 * @return The next pool.
	 */
	private LockableCandidatePool createNextPool()
	{
		switch(settings.getPoolReplacement())
		{
			case AFTER:
				return poolCreation.create(this);
			case IMMEDIATELY:
				return currentPool;
			default:
				throw new IllegalArgumentException("poolReplacement");
		}
	}
	
	/**
	 * @see com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver#getCurrentPool()
	 */
	@Override
	public LockableCandidatePool getCurrentPool()
	{
		return currentPool;
	}
	
	/**
	 * @see com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver#getNextPool()
	 */
	@Override
	public LockableCandidatePool getNextPool()
	{
		return nextPool;
	}
		
	/**
	 * @see com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver#getProblem()
	 */
	@Override
	public DifferentialEvolutionProblem getProblem()
	{
		return problem;
	}
	
	/**
	 * @see com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver#getSettings()
	 */
	@Override
	public DifferentialEvolutionSettings getSettings()
	{
		return settings;
	}
				
	// implementation of the state interface.

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
		currentPool.readLock();
		try 
		{
			return currentPool.getBestCandidate();
		}
		finally 
		{
			currentPool.readUnlock();
		}
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
		return new TimeLength(System.nanoTime() - startTimeInNanos, TimeUnit.NANOSECONDS);
	}

}
