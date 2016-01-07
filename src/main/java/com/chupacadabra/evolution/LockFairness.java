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
package com.chupacadabra.evolution;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock fairnes policy.
 */
public enum LockFairness
{
	
	/**
	 * Fair locking - threads are serviced in the order in which they block.
	 */
	FAIR,
	
	/**
	 * Unfair locking.
	 */
	UNFAIR;
	
	/**
	 * Get the flag value to pass to {@link ReentrantLock}.
	 * 
	 * @return The flag of the aforementioned nature.
	 */
	public boolean getReentrantLockValue() 
	{
		switch(this) 
		{
			case FAIR:
				return true;
			case UNFAIR:
				return false;
			default:
				throw new IllegalArgumentException("this");
		}
	}

}
