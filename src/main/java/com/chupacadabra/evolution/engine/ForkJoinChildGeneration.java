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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

import com.chupacadabra.evolution.Candidate;

/**
 * Fork-join based child generation.
 */
public final class ForkJoinChildGeneration
	implements ChildGeneration
{
	
	/**
	 * @see com.chupacadabra.evolution.engine.ChildGeneration#generate(com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver, int, com.chupacadabra.evolution.Candidate)
	 */
	@Override
	public List<Candidate> generate(
			final DifferentialEvolutionReceiver receiver,
			final int index,
			final Candidate parent) 
	{
		int count = receiver.getSettings().getChildrenPerCandidate();
		List<ForkJoinTask<Candidate>> futures = new ArrayList<ForkJoinTask<Candidate>>(count);

		for(int jindex = 0; jindex < count; jindex++)
		{
			// fork off tasks.
			GenerateChildRecursiveTask recursiveTask = new GenerateChildRecursiveTask(receiver, index, parent);
			futures.add(recursiveTask.fork());
		}

		List<Candidate> children = new ArrayList<Candidate>();
		for(ForkJoinTask<Candidate> future : futures)
		{
			// and collect all non-null results.
			Candidate candidate = future.join();
			if(candidate != null)
			{
				children.add(candidate);
			}
		}

		return children;
	}

}
