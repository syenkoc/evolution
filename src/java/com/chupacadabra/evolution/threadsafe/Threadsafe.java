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

import com.chupacadabra.evolution.RandomSource;

/**
 * Threadsafe decorator provider strategy.
 */
public interface Threadsafe
{

	/**
	 * Get a threadsafe decorator provider that ensures threadsafety using
	 * synchronization.
	 * 
	 * @return A provider of the aforementioned nature.
	 */
	public static Threadsafe synchronization()
	{
		return Synchronization.getInstance();
	}

	/**
	 * Get a threadsafe provider that ensures threadsafety using unfair locking.
	 * 
	 * @return A locking threadsafe provider.
	 */
	public static Threadsafe locking()
	{
		return locking(false);
	}

	/**
	 * Get a threadsafe provider that ensures threadsafety using locking.
	 * 
	 * @param fair The locking fairness.
	 * @return A locking threadsafe provider.
	 */
	public static Threadsafe locking(final boolean fair)
	{
		return Locking.getInstance(fair);
	}

	/**
	 * Create a threadsafe wrapper around the specified object.
	 * 
	 * @param randomSource The object to wrap.
	 * @return A threadsafe view.
	 */
	public RandomSource threadsafe(RandomSource randomSource);

}
