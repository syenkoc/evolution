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
import java.util.concurrent.ExecutionException;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.DiversityPolicy;
import com.chupacadabra.evolution.RandomSource;
import com.chupacadabra.evolution.SelectionPolicy;

/**
 * Core iteration implementation.
 */
public final class IterateIndexAction
	implements Runnable
{
	
	/**
	 * The receiver.
	 */
	private final DifferentialEvolutionReceiver optimizer;
	
	/**
	 * The index to initialize.
	 */
	private final int index;
	
	/**
	 * The parent.
	 */
	private final Candidate parent;
	
	/**
	 * Child generation sub-strategy.
	 */
	private final ChildGeneration childGeneration;
	
	/**
	 * Constructor.
	 * 
	 * @param optimizer The optimizer.
	 * @param index The index of the parent.
	 * @param parent The parent candidate.
	 * @param childGeneration The child generation sub-strategy.
	 */
	public IterateIndexAction(
			final DifferentialEvolutionReceiver optimizer, 
			final int index,
			final Candidate parent,
			final ChildGeneration childGeneration)
	{
		// store the magic.
		this.optimizer = optimizer;
		this.index = index;		
		this.childGeneration = childGeneration;
		this.parent = parent;		
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		try
		{
			runCore();
		}
		catch(final InterruptedException | ExecutionException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Exception throwing run method.
	 * 
	 * @throws InterruptedException If the invoking thread is interrupted.
	 * @throws ExecutionException If a sub-task completes abnormally.
	 */
	private void runCore() 
		throws InterruptedException, ExecutionException
	{
		List<Candidate> children = childGeneration.generate(optimizer, index, parent);
		
		if(children.isEmpty())
		{
			// all children we infeasible. Set the parent in the next pool.
			setNextCandidate(parent);
			return;
		}
		
		DifferentialEvolutionSettings settings = optimizer.getSettings();

		// first, determine the best child.
		SelectionPolicy selectionPolicy = settings.getSelectionPolicy();
		RandomSource randomSource = settings.getRandomSource();
		Candidate bestChild = selectionPolicy.select(optimizer, randomSource, children);
		
		if(isParentBestCandidate())
		{
			// in this case, the child can only replace the parent if it is
			// feasible and has better fitness.
			if(bestChild.isFeasible() && (bestChild.getFitness() < parent.getFitness()))
			{
				setNextCandidate(bestChild);
			}
			else
			{
				setNextCandidate(parent);
			}
			
			// we're done with this case
			return;
		}
		
		// check diversity policy to see if we should compare based only on
		// fitness.
		DiversityPolicy diversityPolicy = settings.getDiversityPolicy();
		double diversity = diversityPolicy.getDiversity(optimizer, randomSource);
		double ud = randomSource.nextDouble();
		
		if(ud < diversity)
		{
			// in this case, we only check the fitness, regardless of the
			// selection policy.
			if(bestChild.getFitness() < parent.getFitness())
			{
				setNextCandidate(bestChild);
			} 
			else
			{
				setNextCandidate(parent);
			}
			
			return;
		}
			
		// otherwise apply the selection policy to get the next candidate.
		Candidate bestCandidate = selectionPolicy.select(optimizer, randomSource, parent, bestChild);
		setNextCandidate(bestCandidate);
	}
	
	/**
	 * Is the parent currently the best candidate?
	 * 
	 * @return <code>true</code> if the parent is the best candidate; and
	 *         <code>false</code> otherwise.
	 */
	private boolean isParentBestCandidate()
	{
		optimizer.getPoolLock().lock(PoolType.CURRENT, LockType.READ);
		try
		{
			return (optimizer.getCurrentPool().getBestCandidateIndex() == index);
		}
		finally
		{
			optimizer.getPoolLock().unlock(PoolType.CURRENT, LockType.READ);
		}
	}

	/**
	 * Set the specified candidate in the next pool.
	 * 
	 * @param nextCandidate The next candidate.
	 */
	private void setNextCandidate(final Candidate nextCandidate)
	{
		optimizer.getPoolLock().lock(PoolType.NEXT, LockType.WRITE);
		try
		{
			optimizer.getNextPool().setCandidate(index, nextCandidate);
		}
		finally
		{
			optimizer.getPoolLock().unlock(PoolType.NEXT, LockType.WRITE);	
		}		
	}
	
}