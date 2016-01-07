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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Fork-join based initializer.
 */
public final class ForkJoinInitialization
	implements Initialization
{
	
	/**
	 * The fork-join pool to use.
	 */
	private final ForkJoinPool forkJoinPool;

	/**
	 * @param forkJoinPool
	 */
	public ForkJoinInitialization(ForkJoinPool forkJoinPool)
	{
		this.forkJoinPool = forkJoinPool;
	}

	/**
	 * @see com.chupacadabra.evolution.engine.Initialization#initialize(com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver)
	 */
	@Override
	public void initialize(final DifferentialEvolutionReceiver receiver)
		throws InterruptedException, ExecutionException
	{
		int size = receiver.getSettings().getCandidatePoolSize();		
		List<ForkJoinTask<?>> futures = new ArrayList<ForkJoinTask<?>>(size);
		
		for(int index = 0; index < size; index++)
		{
			InitializeIndexCommand initializeCommand = new InitializeIndexCommand(receiver, index);
			ForkJoinTask<?> future = forkJoinPool.submit(initializeCommand);
			futures.add(future);
		}

		for(ForkJoinTask<?> future : futures)
		{
			// and wait for each one to finish.
			future.get();
		}
	}
	
}
