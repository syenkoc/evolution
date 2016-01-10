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

import java.util.concurrent.RecursiveTask;

import com.chupacadabra.evolution.Candidate;

/**
 * Child generation as a recursive task.
 */
public class GenerateChildRecursiveTask
	extends RecursiveTask<Candidate>
{

	/**
	 * Serial dumbness.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The receiver.
	 */
	private final DifferentialEvolutionReceiver optimizer;
	
	/**
	 * The parent index.
	 */
	private final int index;
	
	/**
	 * The parent.
	 */
	private final Candidate parent;

	/**
	 * Constructor.
	 * 
	 * @param optimizer The command receiver.
	 * @param index The index.
	 * @param parent The parent. 
	 */
	public GenerateChildRecursiveTask(final DifferentialEvolutionReceiver optimizer,
			final int index, 
			final Candidate parent)
	{
		this.optimizer = optimizer;
		this.index = index;
		this.parent = parent;
	}

	/**
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected Candidate compute()
	{
		GenerateChildTask task = new GenerateChildTask(optimizer, index, parent);
		Candidate child = task.call();
		
		return child;
	}

}
