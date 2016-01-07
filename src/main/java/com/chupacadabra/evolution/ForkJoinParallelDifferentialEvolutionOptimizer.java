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

import java.util.concurrent.ForkJoinPool;

import com.chupacadabra.evolution.engine.ChildGeneration;
import com.chupacadabra.evolution.engine.DifferentialEvolutionEngine;
import com.chupacadabra.evolution.engine.DirectChildGeneration;
import com.chupacadabra.evolution.engine.ForkJoinChildGeneration;
import com.chupacadabra.evolution.engine.ForkJoinInitialization;
import com.chupacadabra.evolution.engine.ForkJoinIteration;
import com.chupacadabra.evolution.engine.Initialization;
import com.chupacadabra.evolution.engine.Iteration;
import com.chupacadabra.evolution.engine.PoolCreation;
import com.chupacadabra.evolution.engine.ReadWriteLockPoolCreation;

/**
 * A parallel differential evolution optimizer that uses a
 * {@linkplain ForkJoinPool fork-join pool} to distribute the work.
 * <p>
 * Instances of this class are safe for use by multiple threads and are also
 * safe for recursive use.
 */
public class ForkJoinParallelDifferentialEvolutionOptimizer
	implements DifferentialEvolutionOptimizer
{

	/**
	 * The fork-join pool to use.
	 */
	private final ForkJoinPool forkJoinPool;

	/**
	 * Constructor.
	 * <p>
	 * This optimizer will use the {@linkplain ForkJoinPool#commonPool() common
	 * pool}.
	 */
	public ForkJoinParallelDifferentialEvolutionOptimizer()
	{
		this(ForkJoinPool.commonPool());
	}

	/**
	 * Constructor.
	 * 
	 * @param forkJoinPool The fork-join pool.
	 */
	public ForkJoinParallelDifferentialEvolutionOptimizer(
			final ForkJoinPool forkJoinPool)
	{
		this.forkJoinPool = forkJoinPool;
	}

	/**
	 * @see com.chupacadabra.evolution.DifferentialEvolutionOptimizer#optimize(com.chupacadabra.evolution.DifferentialEvolutionProblem,
	 *      com.chupacadabra.evolution.DifferentialEvolutionSettings)
	 */
	@Override
	public DifferentialEvolutionResult optimize(
			final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings settings)
	{
		// assemble an engine.
		PoolCreation creation = new ReadWriteLockPoolCreation();
		Initialization initialization = new ForkJoinInitialization(forkJoinPool);
		Iteration iteration = new ForkJoinIteration(forkJoinPool);
		//ChildGeneration childGeneration = new ForkJoinChildGeneration(forkJoinPool);
		ChildGeneration childGeneration = new DirectChildGeneration();
		
		DifferentialEvolutionEngine core = new DifferentialEvolutionEngine(creation, initialization, iteration, childGeneration);

		// and get results.
		DifferentialEvolutionResult result = core.getResult(problem, settings);

		return result;
	}

}
