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
import java.util.concurrent.RecursiveAction;

/**
 * Fork-join iteration in-pool action.
 */
public final class IterateRecursiveAction
	extends RecursiveAction
{

	/**
	 * Serial version. 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The receiver.
	 */
	private final DifferentialEvolutionReceiver receiver;
		
	/**
	 * Child generation strategy.
	 */
	private final ChildGeneration childGeneration;
	
	/**
	 * @param receiver
	 * @param childGeneration
	 */
	public IterateRecursiveAction(
			final DifferentialEvolutionReceiver receiver,
			final ChildGeneration childGeneration)
	{
		this.receiver = receiver;
		this.childGeneration = childGeneration;
	}

	/**
	 * @see java.util.concurrent.RecursiveAction#compute()
	 */
	@Override
	protected void compute()
	{
		int size = receiver.getSettings().getCandidatePoolSize();
		List<ForkJoinTask<Void>> futures = new ArrayList<ForkJoinTask<Void>>(size);
		
		for(int index = 0; index < size; index++)
		{
			// fork off a request for each index.
			IterateIndexRecursiveAction iterateAction = new IterateIndexRecursiveAction(receiver, index, childGeneration);
			iterateAction.fork();			
		}
		
		for(ForkJoinTask<Void> future : futures)
		{
			// and now join.
			future.join();
		}		
	}

}
