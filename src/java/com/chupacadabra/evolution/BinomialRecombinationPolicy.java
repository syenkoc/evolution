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
 * The binomial recombination policy.
 */
public final class BinomialRecombinationPolicy 
	implements RecombinationPolicy
{

	/**
	 * Default fixed crossover: {@value}
	 */
	public static final double DEFAULT_CROSSOVER = 0.9d;
	
	/**
	 * The crossover policy.
	 */
	private final CrossoverPolicy crossoverPolicy;
	
	/**
	 * Default constructor.
	 * <p>
	 * Uses the default fixed crossover.
	 */
	public BinomialRecombinationPolicy()
	{
		this(new FixedCrossoverPolicy(DEFAULT_CROSSOVER));
	}
	
	/**
	 * Constructor.
	 * 
	 * @param crossoverPolicy The crossover policy to use.
	 */
	public BinomialRecombinationPolicy(final CrossoverPolicy crossoverPolicy)
	{
		this.crossoverPolicy = crossoverPolicy;
	}
	
	/**
	 * @see com.chupacadabra.evolution.RecombinationPolicy#recombine(com.chupacadabra.evolution.DifferentialEvolutionState, com.chupacadabra.evolution.RandomSource, double[], double[])
	 */
	@Override
	public double[] recombine(
			final DifferentialEvolutionState state,
			final RandomSource randomSource, 
			final double[] parent, 
			final double[] trial)
	{
		// create an index that we'll always change.
		int dimension = state.getDimension();
		int j = randomSource.nextInt(dimension);
		
		// get the crossover weight.
		double cr = crossoverPolicy.getCrossover(state, randomSource);
		
		// do the core binomial recombination algorithm!
		double[] child = new double[dimension];
		for(int index = 0; index < dimension; index++)
		{
			double r = randomSource.nextDouble();
			child[index] = ((r < cr) || (index == j)) ? trial[index] : parent[index]; 			
		}		
		
		return child;
	}

}
