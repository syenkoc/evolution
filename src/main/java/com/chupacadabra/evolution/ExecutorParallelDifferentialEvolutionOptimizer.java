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

import java.util.concurrent.Executor;

import com.chupacadabra.evolution.engine.ChildGeneration;
import com.chupacadabra.evolution.engine.DifferentialEvolutionEngine;
import com.chupacadabra.evolution.engine.DirectChildGeneration;
import com.chupacadabra.evolution.engine.ExecutorInitialization;
import com.chupacadabra.evolution.engine.ExecutorIteration;
import com.chupacadabra.evolution.engine.Initialization;
import com.chupacadabra.evolution.engine.Iteration;
import com.chupacadabra.evolution.engine.PoolCreation;
import com.chupacadabra.evolution.engine.ReadWriteLockPoolCreation;

/**
 * A parallel differential optimizer that uses an underlying executor to
 * split work across multiple threads.
 * <p>
 * Instances of this class are safe for use by multiple threads, but they
 * are <b>not</b> safe for recursive use (<i>i.e.</i> an optimization performing a
 * "sub-optimization" using the same optimizer instance).
 */
public final class ExecutorParallelDifferentialEvolutionOptimizer
	implements DifferentialEvolutionOptimizer
{
	
	/**
	 * The executor to use.
	 */
	private final Executor executor;

	/**
	 * Constructor.
	 * 
	 * @param executor The executor.
	 */
	public ExecutorParallelDifferentialEvolutionOptimizer(final Executor executor)
	{
		this.executor = executor;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionOptimizer#optimize(com.chupacadabra.evolution.DifferentialEvolutionProblem, com.chupacadabra.evolution.DifferentialEvolutionSettings)
	 */
	@Override
	public DifferentialEvolutionResult optimize(
			final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings settings)
	{
		// make the appropriate strategies.
		PoolCreation creation = new ReadWriteLockPoolCreation();
		Initialization initialization = new ExecutorInitialization(executor);
		Iteration iteration = new ExecutorIteration(executor);
		ChildGeneration childGeneration = new DirectChildGeneration();
		
		// build up the engine.
		DifferentialEvolutionEngine core = new DifferentialEvolutionEngine(creation, initialization, iteration, childGeneration);
		
		// and now just grab the result from it!
		DifferentialEvolutionResult result = core.getResult(problem, settings);
		
		return result;
	}

}
