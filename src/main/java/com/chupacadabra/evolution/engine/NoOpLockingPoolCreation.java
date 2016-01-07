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

import com.chupacadabra.evolution.pool.ArrayCandidatePool;
import com.chupacadabra.evolution.pool.LockableCandidatePool;
import com.chupacadabra.evolution.pool.NoOpLockableCandidatePool;

/**
 * Pool creator that returns a pool that does no actual locking.
 */
public final class NoOpLockingPoolCreation
	implements PoolCreation
{

	/**
	 * @see com.chupacadabra.evolution.engine.PoolCreation#create(com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver)
	 */
	@Override
	public LockableCandidatePool create(DifferentialEvolutionReceiver receiver)
	{
		int size = receiver.getSettings().getCandidatePoolSize();
		ArrayCandidatePool arrayPool = new ArrayCandidatePool(size);
		
		// use the pool that doesn't do actual locking!
		NoOpLockableCandidatePool nopPool = new NoOpLockableCandidatePool(arrayPool);
		
		return nopPool;
	}

}
