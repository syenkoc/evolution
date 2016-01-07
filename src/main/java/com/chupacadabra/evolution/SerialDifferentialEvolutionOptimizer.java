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

import com.chupacadabra.evolution.engine.ChildGeneration;
import com.chupacadabra.evolution.engine.DifferentialEvolutionEngine;
import com.chupacadabra.evolution.engine.DirectChildGeneration;
import com.chupacadabra.evolution.engine.DirectInitialization;
import com.chupacadabra.evolution.engine.DirectIteration;
import com.chupacadabra.evolution.engine.Initialization;
import com.chupacadabra.evolution.engine.Iteration;
import com.chupacadabra.evolution.engine.NoOpLockingPoolCreation;
import com.chupacadabra.evolution.engine.PoolCreation;

/**
 * A serial differential optimizer.
 * <p>
 * Instances of this class are safe for use by multiple threads and are also
 * safe for recursive use.
 */
public final class SerialDifferentialEvolutionOptimizer
	implements DifferentialEvolutionOptimizer
{

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionOptimizer#optimize(com.chupacadabra.evolution.DifferentialEvolutionProblem, com.chupacadabra.evolution.DifferentialEvolutionSettings)
	 */
	@Override
	public DifferentialEvolutionResult optimize(
			final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings settings)
	{		
		// build up an engine.
		PoolCreation poolCreation = new NoOpLockingPoolCreation();
		Initialization initialization = new DirectInitialization();
		Iteration iteration = new DirectIteration();		
		ChildGeneration childGeneration = new DirectChildGeneration();
		DifferentialEvolutionEngine engine = new DifferentialEvolutionEngine(poolCreation, initialization, iteration, childGeneration);
		
		// get results.
		DifferentialEvolutionResult result = engine.getResult(problem, settings);
		
		return result;
	}

}
