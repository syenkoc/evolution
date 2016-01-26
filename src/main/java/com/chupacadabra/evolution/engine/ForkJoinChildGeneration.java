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

import java.util.List;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.ForkJoinDifferentialEvolutionOptimizerConfiguration;

/**
 * Fork-join based child generation.
 */
public final class ForkJoinChildGeneration
	implements ChildGeneration
{
	
	/**
	 * The configuration.
	 */
	private final ForkJoinDifferentialEvolutionOptimizerConfiguration configuration;
	
	/**
	 * Constructor.
	 * 
	 * @param configuration The configuration.
	 */
	public ForkJoinChildGeneration(
			ForkJoinDifferentialEvolutionOptimizerConfiguration configuration)
	{
		this.configuration = configuration;
	}

	/**
	 * @see com.chupacadabra.evolution.engine.ChildGeneration#generate(com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver, int, com.chupacadabra.evolution.Candidate)
	 */
	@Override
	public List<Candidate> generate(
			final DifferentialEvolutionReceiver receiver,
			final int index,
			final Candidate parent) 
	{
		// construct a core fork-join task.
		int count = receiver.getSettings().getChildrenPerCandidate();
		ForkJoinChildGenerationRecursiveTask task = new ForkJoinChildGenerationRecursiveTask(configuration, receiver, 0, count, index, parent);
		
		// and execute it, knowing that we're already inside a fork-join pool.
		List<Candidate> candidates = task.invoke();
		
		return candidates;		
	}

}
