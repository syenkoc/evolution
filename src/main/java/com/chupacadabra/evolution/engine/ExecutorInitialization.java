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
import java.util.concurrent.Executor;

/**
 * Executor-based initialization strategy.
 */
public final class ExecutorInitialization
	implements Initialization
{
	
	/**
	 * The executor.
	 */
	private final Executor executor;
	
	/**
	 * Constructor.
	 * 
	 * @param executor The executor in which to enqueue the tasks.
	 */
	public ExecutorInitialization(final Executor executor)
	{
		this.executor = executor;
	}

	/**
	 * @see com.chupacadabra.evolution.engine.Initialization#initialize(com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver)
	 */
	@Override
	public void initialize(final DifferentialEvolutionReceiver receiver)
		throws InterruptedException
	{
		int size = receiver.getSettings().getCandidatePoolSize();
		CountDownLatch latch = new CountDownLatch(size);
		
		for(int index = 0; index < size; index++)
		{
			// enqueue a task for each index in the pool.
			InitializeIndexCommand initializeCommand = new InitializeIndexCommand(receiver, index);
			ReportExceptionCommand reportCommand = new ReportExceptionCommand(receiver, initializeCommand);
			CountDownCommand countDownCommand = new CountDownCommand(reportCommand, latch);
			executor.execute(countDownCommand);
		}
		
		// wait for all initialization commands to finish.
		latch.await();
	}

}
