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

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.FeasibilityFunction;
import com.chupacadabra.evolution.FeasibilityType;
import com.chupacadabra.evolution.FitnessFunction;
import com.chupacadabra.evolution.RandomParametersFunction;
import com.chupacadabra.evolution.RandomSource;

/**
 * Core initialization action.
 */
public final class InitializeIndexAction
	implements Runnable
{
	
	/**
	 * The receiver.
	 */
	private final DifferentialEvolutionReceiver optimizer;
	
	/**
	 * The index to initialize.
	 */
	private final int index;

	/**
	 * Constructor.
	 * 
	 * @param optimizer The optimizer.
	 * @param index The index.
	 */
	public InitializeIndexAction(final DifferentialEvolutionReceiver optimizer,
			final int index)
	{
		this.optimizer = optimizer;
		this.index = index;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		// generate a feasible candidate.
		Candidate candidate = generateFeasibleCandidate();
		
		optimizer.getPoolLock().lock(PoolType.CURRENT, LockType.WRITE);
		try
		{
			// and store in the current pool at the desired index.
			optimizer.getCurrentPool().setCandidate(index, candidate);
		}
		finally
		{
			optimizer.getPoolLock().unlock(PoolType.CURRENT, LockType.WRITE);
		}		
	}
	
	/**
	 * Generate a feasible candidate.
	 * 
	 * @return A feasible candidate.
	 */
	private Candidate generateFeasibleCandidate()
	{
		// extract problem functions.
		DifferentialEvolutionProblem problem = optimizer.getProblem();
		FitnessFunction fitnessFunction = problem.getFitnessFunction();
		RandomParametersFunction randomParametersFunction = problem.getRandomParametersFunction();
		FeasibilityFunction feasibilityFunction = problem.getFeasibilityFunction();
		
		// and get the random source from the settings.
		RandomSource randomSource = optimizer.getSettings().getRandomSource();
		
		// we should probably think about having some kind of maximum iteration
		// count... A poorly implemented random parameter function (or a
		// pathological domain) could cause this to loop indefinitely.
		generating:
		while(true)
		{
			// create a set of parameters.
			double[] randomParameters = randomParametersFunction.createRandomParameters(randomSource);
			
			// use the feasibility function to classify it.
			FeasibilityType feasibility = feasibilityFunction.getFeasibilityType(randomParameters);
			
			switch(feasibility)
			{
				case FEASIBLE:
					// we found a feasible vector. Measure the fitness and return it.
					double fitness = fitnessFunction.getFitness(randomParameters);
					Candidate candidate = Candidate.feasible(randomParameters, fitness);
					
					return candidate;
				case INFEASIBLE:
				case VIOLATING:
					continue generating;
				default:
					throw new IllegalArgumentException("feasbility");
			}			
		}		
	}

}
