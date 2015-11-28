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
 * <i>n</i>-orthotope random parameter function.
 */
public class NOrthotopeRandomParametersFunction 
	implements RandomParametersFunction
{

	/**
	 * The problem dimension.
	 */
	private final int dimension;
	
	/**
	 * The boxes.
	 */
	private final double[][] boxes;
	
	/**
	 * Constructor.
	 * 
	 * @param dimension The problem dimension.
	 */
	public NOrthotopeRandomParametersFunction(final int dimension)
	{
		this.dimension = dimension;
		this.boxes = new double[dimension][];
	}
	
	/**
	 * Set the parameter range for the specified variable.
	 * 
	 * @param index The variable index.
	 * @param lowerBound The lower bound.
	 * @param upperBound The upper bound.
	 */
	public void setParameterRange(final int index, final double lowerBound, final double upperBound)
	{
		boxes[index] = new double[]{lowerBound, upperBound};
	}

	/**
	 * @see com.chupacadabra.evolution.RandomParametersFunction#createRandomParameters(com.chupacadabra.evolution.RandomSource)
	 */
	@Override
	public double[] createRandomParameters(final RandomSource randomSource)
	{
		double[] parameters = new double[dimension];
		for(int index = 0; index < dimension; index++)
		{
			double[] box = boxes[index];
			double min = box[0];
			double max = box[1];
			double randomValue = min + ((max - min) * randomSource.nextDouble());
			parameters[index] = randomValue;
		}
		
		return parameters;
	}

}
