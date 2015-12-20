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

import java.util.concurrent.RecursiveTask;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.DifferentialEvolutionState;
import com.chupacadabra.evolution.DifferentiationPolicy;
import com.chupacadabra.evolution.FeasibilityFunction;
import com.chupacadabra.evolution.FeasibilityType;
import com.chupacadabra.evolution.FitnessFunction;
import com.chupacadabra.evolution.RandomSource;
import com.chupacadabra.evolution.RecombinationPolicy;
import com.chupacadabra.evolution.ViolationFunction;
import com.chupacadabra.evolution.pool.CandidatePool;

/**
 * Create a child candidate task.
 */
final class CreateChildCandidateTask
	extends RecursiveTask<Candidate> 
{

	/**
	 * Serial ID.
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
	 * The state.
	 */
	private final DifferentialEvolutionState state;

	/**
	 * The parent index.
	 */
	private final int index;
	
	/**
	 * Candidate pool.
	 */
	private final CandidatePool candidatePool;
	
	/**
	 * Constuctor.
	 * 
	 * @param problem The problem.
	 * @param policies The policies.
	 * @param state The state.
	 * @param index The parent index.
	 * @param candidatePool The candidate pool.
	 */
	CreateChildCandidateTask(final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings policies,
			final DifferentialEvolutionState state, 
			final int index,
			final CandidatePool candidatePool)
	{
		this.problem = problem;
		this.settings = policies;
		this.state = state;
		this.index = index;
		this.candidatePool = candidatePool;
	}

	/**
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected Candidate compute()
	{
		// grab the parent.
		Candidate parent = candidatePool.getCandidate(index);
		double[] parentParameters = parent.getParameters();
		
		// first, differentiate to get trial parameters.
		RandomSource randomSource = settings.getRandomSource();
		DifferentiationPolicy diffentiationPolicy = settings.getDifferentiationPolicy();		
		double[] trial = diffentiationPolicy.differentiate(state, randomSource, parent, index, candidatePool);
		
		// perform recombination to get child parameters.
		RecombinationPolicy recombinationPolicy = settings.getRecombinationPolicy();
		double[] child = recombinationPolicy.recombine(state, randomSource, parentParameters, trial);
		
		// classify the child parameters.
		FeasibilityFunction feasibilityFunction = problem.getFeasibilityFunction();
		FeasibilityType feasibility = feasibilityFunction.getFeasibilityType(child);
		
		if(feasibility == FeasibilityType.INFEASIBLE) 
		{
			// we can short-circuit fitness calculation.
			return null;
		}
		
		// measure fitness of child parameters.
		FitnessFunction fitnessFunction = problem.getFitnessFunction();
		double childFitness = fitnessFunction.getFitness(child);
		
		switch(feasibility) 
		{
			case FEASIBLE:
				return Candidate.feasible(child, childFitness);
			case VIOLATING:
				// in this case we also have to measure the violation.
				ViolationFunction violationFunction = problem.getViolationFunction();
				double violation = violationFunction.getViolation(child);
				
				return Candidate.violating(child, childFitness, violation);
			case INFEASIBLE:
			default:
				// impossible.
				throw new InternalError();
		}	
	}

}
