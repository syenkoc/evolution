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

import java.util.concurrent.ForkJoinPool;

import com.chupacadabra.evolution.DifferentialEvolutionOptimizer;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.DifferentialEvolutionResult;

/**
 * A multi-threaded differential evolution optimizer that uses a fork/join pool.
 * <p>
 * Instances of this class are safe for use by multiple threads.
 */
public class ForkJoinDifferentialEvolutionOptimizer
	implements DifferentialEvolutionOptimizer
{

	/**
	 * The fork-join pool to use.
	 */
	private final ForkJoinPool forkJoinPool;

	/**
	 * Constructor.
	 * <p>
	 * This constructor will use the {@linkplain ForkJoinPool#commonPool()
	 * common pool} for execution.
	 */
	public ForkJoinDifferentialEvolutionOptimizer()
	{
		this(ForkJoinPool.commonPool());
	}

	/**
	 * Constructor.
	 * 
	 * @param forkJoinPool The pool to use.
	 */
	public ForkJoinDifferentialEvolutionOptimizer(
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
			final DifferentialEvolutionSettings policies)
	{
		// create a NEW core optimizer.
		ForkJoinDifferentialEvolutionOptimizerCore core = new ForkJoinDifferentialEvolutionOptimizerCore(forkJoinPool, problem, policies);
		
		// and now just get the results.
		DifferentialEvolutionResult result = core.optimize();
		
		return result;
	}

}
