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

import java.util.Collections;
import java.util.List;

/**
 * Rosenbrock function problem.
 */
public class RosenbrockFunctionProblem
	implements DifferentialEvolutionProblem
{
	
	/**
	 * The function.
	 */
	private final RosenbrockFunction rosenbrockFunction;
	
	/**
	 * Constructor.
	 * 
	 * @param rosenbrockFunction The Rosenbrock function.
	 */
	public RosenbrockFunctionProblem(final RosenbrockFunction rosenbrockFunction)
	{
		this.rosenbrockFunction = rosenbrockFunction;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getDimension()
	 */
	@Override
	public int getDimension()
	{
		return 2;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getRandomParametersFunction()
	 */
	@Override
	public RandomParametersFunction getRandomParametersFunction()
	{
		NOrthotopeRandomParametersFunction nOrthotope = new NOrthotopeRandomParametersFunction(2);
		nOrthotope.setParameterRange(0, -1000000, 1000000);
		nOrthotope.setParameterRange(1, -1000000, 1000000);
		
		return nOrthotope;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getFitnessFunction()
	 */
	@Override
	public FitnessFunction getFitnessFunction()
	{
		return rosenbrockFunction;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getTerminationCriteria()
	 */
	@Override
	public List<TerminationCriterion> getTerminationCriteria()
	{
		return Collections.singletonList(new FitnessAchieved(1e-16d));
	}

}
