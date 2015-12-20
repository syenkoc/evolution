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
 * Pairwise weighted parameter sum utility class.
 * <p>
 * This class is stateless - and hence safe for use by multiple threads - and
 * cannot be instantiated.
 */
final class PairwiseWeightedParameterSum
{

	/**
	 * Perform weighted pair-wise sum of the specified candidates.
	 * 
	 * @param f The weight.
	 * @param trial The vector in which to store the results.
	 * @param offset Array offset.
	 * @param candidates The candidates.
	 */
	static void computeInPlace(final double f, 
			final double[] trial,
			final int offset,
			final Candidate... candidates)
	{
		int total = candidates.length;
		int dimension = trial.length;

		for(int index = offset; index < total; index += 2)
		{
			double[] x1 = candidates[index].getParametersReference();
			double[] x2 = candidates[(index + 1)].getParametersReference();

			for(int jindex = 0; jindex < dimension; jindex++)
			{
				trial[jindex] += f * (x1[jindex] - x2[jindex]);
			}
		}
	}
	
	/**
	 * 
	 */
	private PairwiseWeightedParameterSum()
	{
	}
}
