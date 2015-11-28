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

/**
 * A source of randomness.
 * <p>
 * This class is basically an abstraction of {@link java.util.Random}.
 */
public interface RandomSource
{

	/**
	 * Get the next psuedo-random integer, uniformly distributed in
	 * <code>[0, n)</code>.
	 * 
	 * @param n The (exclusive) max.
	 * @return The next integer.
	 */
	public int nextInt(int n);

	/**
	 * Get the next psuedo-random boolean.
	 *
	 * @return The next boolean.
	 */
	public default boolean nextBoolean()
	{
		return (nextInt(1) == 0);
	}

	/**
	 * Get the next pseudo-random double, uniformly distributed in
	 * <code>[0, 1]</code>.
	 * 
	 * @return The next double.
	 */
	public double nextDouble();

	/**
	 * Get the next pseudo-random Gaussian (standard normal, <i>i.e.</i> normal
	 * with mean 0 and standard deviation 1).
	 * 
	 * @return The next Gaussian.
	 */
	public double nextGaussian();

}
