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

/**
 * Hacky runnable to store a child.
 */
public final class GenerateChildCommand
	implements Runnable
{
	
	/**
	 * The optimizer.
	 */
	private final DifferentialEvolutionReceiver optimizer;
	
	/**
	 * The index.
	 */
	private final int index;
	
	/**
	 * The children.
	 */
	private final List<Candidate> children;
	
	/**
	 * Constructor.
	 * 
	 * @param optimizer The receiver.
	 * @param index The index.
	 * @param children Result array.
	 */
	public GenerateChildCommand(
			final DifferentialEvolutionReceiver optimizer,
			final int index, 
			final List<Candidate> children)
	{
		this.optimizer = optimizer;
		this.index = index;
		this.children = children;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		GenerateChildTask task = new GenerateChildTask(optimizer, index);
		Candidate child = task.call();
		if(child != null)
		{
			children.add(child);
		}
	}

}
