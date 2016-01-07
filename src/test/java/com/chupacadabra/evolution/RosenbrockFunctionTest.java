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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chupacadabra.evolution.util.JavaUtilRandomSource;


/**
 * 
 */
public class RosenbrockFunctionTest
{
	
	public static void main(String[] args)
	{
		DifferentialEvolutionOptimizer optimizer = new ForkJoinParallelDifferentialEvolutionOptimizer();
		
		ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		//ExecutorService es = Executors.newCachedThreadPool();
		DifferentialEvolutionOptimizer optimizer2 = new ExecutorParallelDifferentialEvolutionOptimizer(es);

		DifferentialEvolutionOptimizer optimizer3 = new SerialDifferentialEvolutionOptimizer();
		
		solve(optimizer);
		solve(optimizer2);
		solve(optimizer3);
				
		es.shutdown();		
	}
	
	static void solve(final DifferentialEvolutionOptimizer optimizer)
	{
		RosenbrockFunctionProblem problem = new RosenbrockFunctionProblem(new RosenbrockFunction(1, 100));
		DifferentialEvolutionSettings settings = new DifferentialEvolutionSettings();
		settings.setMaximumGeneration(10000);
		settings.setRandomSource(new JavaUtilRandomSource(1));

		// now just get the result!
		DifferentialEvolutionResult result = optimizer.optimize(problem, settings);
		
		// extract x and f(x).
		double[] x = result.getBestCandidate().getParameters();
		double fx = result.getBestCandidate().getFitness();
		
		System.out.println(x[0] + " " + x[1]);
		System.out.println(fx);
		System.out.println(result.getTimeTaken());

	}

}
