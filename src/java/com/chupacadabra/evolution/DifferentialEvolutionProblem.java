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

/**
 * A differential evolution problem.
 */
public interface DifferentialEvolutionProblem
{

	/**
	 * Get the dimension of the problem.
	 * 
	 * @return The dimension.
	 */
	public int getDimension();
	
	/**
	 * Get the function that creates random parameter vectors.
	 * 
	 * @return The random parameter vector function.
	 */
	public RandomParametersFunction getRandomParametersFunction();

	/**
	 * Get the fitness function.
	 * 
	 * @return The fitness function.
	 */
	public FitnessFunction getFitnessFunction();

	/**
	 * Get the feasibility function.
	 * <p>
	 * This default implementation returns a function that classifies all
	 * candidates as feasible.
	 * 
	 * @return The feasibility function.
	 */
	public default FeasibilityFunction getFeasibilityFunction()
	{
		return (double[] candidate) -> FeasibilityType.FEASIBLE;
	}

	/**
	 * Get the violation function.
	 * <p>
	 * The default implementation returns a function that assigns <code>0</code>
	 * to all candidates.
	 * 
	 * @return The violation function.
	 */
	public default ViolationFunction getViolationFunction()
	{
		return (double[] candidate) -> 0;
	}

}
