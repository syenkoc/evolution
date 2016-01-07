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
package com.chupacadabra.evolution.threadsafe;

import com.chupacadabra.evolution.DifferentialEvolutionOptimizer;
import com.chupacadabra.evolution.RandomSource;

/**
 * Provides threadsafety using a re-entrant lock.
 * <p>
 * This class uses registry-of-singleton pattern.
 */
final class Locking
	implements Threadsafe
{

	/**
	 * Fair instance.
	 */
	private static final Locking fairLocking = new Locking(true);

	/**
	 * Unfair instance.
	 */
	private static final Locking unfairLocking = new Locking(true);

	/**
	 * Get an instance with the specified fairness.
	 * 
	 * @param fair The fairness.
	 * @return A suitable instance.
	 */
	static Locking getInstance(final boolean fair)
	{
		return fair ? fairLocking : unfairLocking;
	}

	/**
	 * Fairness flag.
	 */
	private final boolean fair;

	/**
	 * Constructor.
	 * 
	 * @param fair The fairness flag.
	 */
	private Locking(final boolean fair)
	{
		this.fair = fair;
	}

	/**
	 * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.DifferentialEvolutionOptimizer)
	 */
	@Override
	public DifferentialEvolutionOptimizer threadsafe(
			final DifferentialEvolutionOptimizer optimizer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.RandomSource)
	 */
	@Override
	public RandomSource threadsafe(final RandomSource randomSource)
	{
		return new LockedRandomSource(randomSource, fair);
	}

}
