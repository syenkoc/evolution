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
 * The exponential recombination policy.
 */
public final class ExponentialRecombinationPolicy implements RecombinationPolicy {

    /**
     * Default fixed crossover: {@value}
     */
    public static final double DEFAULT_CROSSOVER = 0.95d;

    /**
     * The crossover policy.
     */
    private final CrossoverPolicy crossoverPolicy;

    /**
     * Default constructor.
     * <p>
     * Uses the default fixed crossover.
     */
    public ExponentialRecombinationPolicy() {
        this(new FixedCrossoverPolicy(DEFAULT_CROSSOVER));
    }

    /**
     * Constructor.
     * 
     * @param crossoverPolicy The crossover policy to use.
     */
    public ExponentialRecombinationPolicy(final CrossoverPolicy crossoverPolicy) {
        this.crossoverPolicy = crossoverPolicy;
    }

    /**
     * @see com.chupacadabra.evolution.RecombinationPolicy#recombine(com.chupacadabra.evolution.DifferentialEvolutionState,
     *      com.chupacadabra.evolution.RandomSource, double[], double[])
     */
    @Override
    public double[] recombine(final DifferentialEvolutionState state, final RandomSource randomSource, final double[] parent, final double[] trial) {
        int dimension = state.getDimension();
        int j = randomSource.nextInt(dimension);

        double[] child = parent.clone();
        double crossover = crossoverPolicy.getCrossover(state, randomSource);

        // loop variables.
        double r;
        int l = 0;

        do {
            child[j] = trial[j];

            // update the index variables.
            j = (j + 1) % dimension;
            l += 1;
            r = randomSource.nextDouble();
        } while ((r < crossover) && (l < dimension));

        return child;
    }

}
