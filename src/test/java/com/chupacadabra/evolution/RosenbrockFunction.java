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
 * The bivariate Rosenbrock optimization test function.
 */
public class RosenbrockFunction 
	implements FitnessFunction
{
	
	/**
	 * <code>a</code>
	 */
	private final double a;
	
	/**
	 * <code>b</code>
	 */
	private final double b;

	/**
	 * C
	 * 
	 * @param a
	 * @param b
	 */
	public RosenbrockFunction(double a, double b)
	{
		this.a = a;
		this.b = b;
	}
	
	/**
	 * @see com.chupacadabra.evolution.FitnessFunction#getFitness(double[])
	 */
	@Override
	public double getFitness(double[] p)
	{
		double x = p[0];
		double y = p[1];
		
		double amx = a - x;
		double ymx2 = y - (x * x);		
		double value = (amx * amx) + (b * ymx2 * ymx2);
		
		return value;
	}

}
