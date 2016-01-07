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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * Executor-based iteration.
 */
public final class ExecutorIteration
	implements Iteration
{
	
	/**
	 * The executor.
	 */
	private final Executor executor;
	
	/**
	 * Constructor.
	 * 
	 * @param executor The execuctor.
	 */
	public ExecutorIteration(final Executor executor)
	{
		this.executor = executor;
	}

	/**
	 * @see com.chupacadabra.evolution.engine.Iteration#iterate(com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver, com.chupacadabra.evolution.engine.ChildGeneration)
	 */
	@Override
	public void iterate(final DifferentialEvolutionReceiver receiver,
			final ChildGeneration childGeneration)
		throws InterruptedException, ExecutionException
	{
		int size = receiver.getSettings().getCandidatePoolSize();
		CountDownLatch latch = new CountDownLatch(size);
		
		for(int index = 0; index < size; index++)
		{
			// build up a command to perform iteration, report exception, and count down the latch.
			IterateIndexCommand iterateCommand = new IterateIndexCommand(receiver, index, childGeneration);
			ReportExceptionCommand exceptionCommand = new ReportExceptionCommand(receiver, iterateCommand);
			CountDownCommand countDownCommand = new CountDownCommand(exceptionCommand, latch);
			
			// and enqueue the command!
			executor.execute(countDownCommand);
		}
		
		// wait for all commands to finish.
		latch.await();		
	}

}
