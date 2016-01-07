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

/**
 * This command counts down a latch once an underlying task completes.
 * <p>
 * This class uses the decorator design pattern.
 */
public final class CountDownCommand
	implements Runnable
{

	/**
	 * The underlying command to run.
	 */
	private final Runnable command;

	/**
	 * The latch to count down.
	 */
	private final CountDownLatch latch;

	/**
	 * Constructor.
	 * 
	 * @param command The command to run.
	 * @param latch The latch.
	 */
	public CountDownCommand(final Runnable command, final CountDownLatch latch)
	{
		this.command = command;
		this.latch = latch;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		try
		{
			command.run();
		}
		finally
		{
			latch.countDown();
		}		
	}
	
}
