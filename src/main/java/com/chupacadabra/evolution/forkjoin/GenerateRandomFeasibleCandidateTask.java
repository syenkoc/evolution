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

import java.util.concurrent.RecursiveAction;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.FeasibilityFunction;
import com.chupacadabra.evolution.FeasibilityType;
import com.chupacadabra.evolution.FitnessFunction;
import com.chupacadabra.evolution.RandomParametersFunction;
import com.chupacadabra.evolution.RandomSource;
import com.chupacadabra.evolution.pool.CandidatePool;

/**
 * Task to generate a random feasible candidate and insert it into the pool.
 */
final class GenerateRandomFeasibleCandidateTask
	extends RecursiveAction
{

	/**
	 * Serial dumbness.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The problem.
	 */
	private final DifferentialEvolutionProblem problem;
	
	/**
	 * The settings.
	 */
	private final DifferentialEvolutionSettings settings;
	
	/**
	 * The index in the pool, for which to generate the candidate.
	 */
	private final int index;
	
	/**
	 * The pool.
	 */
	private final CandidatePool candidatePool;
	
	/**
	 * Constructor.
	 * 
	 * @param problem The problem.
	 * @param settings The settings.
	 * @param index The index.
	 * @param candidatePool The candidate pool.
	 */
	GenerateRandomFeasibleCandidateTask(
			final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings settings, 
			final int index,
			final CandidatePool candidatePool)
	{
		this.problem = problem;
		this.settings = settings;
		this.index = index;
		this.candidatePool = candidatePool;
	}
	
	/**
	 * @see java.util.concurrent.RecursiveAction#compute()
	 */
	@Override
	protected void compute()
	{
		RandomParametersFunction parameterFunction = problem.getRandomParametersFunction();
		FeasibilityFunction feasibilityFunction = problem.getFeasibilityFunction();
		RandomSource randomSource = settings.getRandomSource();
		
		generating:
		while(true) 
		{
			// create a parameter vector.
			double[] parameters = parameterFunction.createRandomParameters(randomSource);
			
			// check feasibility.
			FeasibilityType feasibilityType = feasibilityFunction.getFeasibilityType(parameters);
			
			switch(feasibilityType)
			{
				case FEASIBLE:
					FitnessFunction fitnessFunction = problem.getFitnessFunction();
					double fitness = fitnessFunction.getFitness(parameters);
					
					// now just wrap up in candidate and store in the pool.
					Candidate candidate = Candidate.feasible(parameters, fitness);
					candidatePool.setCandidate(index, candidate);
					
					break generating;
				case INFEASIBLE:
				case VIOLATING:
				default:
					continue generating;
			}
		}
	}
	
}
