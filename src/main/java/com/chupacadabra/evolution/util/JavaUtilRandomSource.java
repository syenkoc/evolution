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
package com.chupacadabra.evolution.util;

import java.util.Random;

import com.chupacadabra.evolution.RandomSource;


/**
 * A simple random source based on {@link java.util.Random}.
 * <p>
 * Instances of this class are safe for use by multiple threads.
 */
public final class JavaUtilRandomSource
	implements RandomSource
{
	
	/**
	 * The underlying random.
	 */
	private final Random random;
	
	/**
	 * Constructor.
	 */
	public JavaUtilRandomSource()
	{
		random = new Random();
	}

	/**
	 * Constructor.
	 * 
	 * @param seed The seed for the random.
	 */
	public JavaUtilRandomSource(final long seed)
	{
		random = new Random(seed);
	}

	/**
	 * @see com.chupacadabra.evolution.RandomSource#nextInt(int)
	 */
	@Override
	public int nextInt(final int n)
	{
		return random.nextInt(n);
	}

	/**
	 * @see com.chupacadabra.evolution.RandomSource#nextDouble()
	 */
	@Override
	public double nextDouble()
	{
		return random.nextDouble();
	}

	/**
	 * @see com.chupacadabra.evolution.RandomSource#nextGaussian()
	 */
	@Override
	public double nextGaussian()
	{
		return random.nextGaussian();
	}

}
