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

import com.chupacadabra.evolution.pool.CandidatePool;

/**
 * The multinomial differentiation policy due to Buhry <i>et al</i>.
 * <p>
 * The fitness measure function must return strictly positive values if this
 * policy is used.
 */
public final class MultinomialDifferentiationPolicy implements DifferentiationPolicy {

    /**
     * Default &alpha; value: {@value}
     */
    public static final double DEFAULT_ALPHA = 0.5d;

    /**
     * Default vector pair count: {@value}
     */
    public static final int DEFAULT_COUNT = 1;

    /**
     * Default multinomial weight: {@value}
     */
    public static final double DEFAULT_MULTINOMIAL_WEIGHT = .8d;

    /**
     * Default random weight: {@value}
     */
    public static final double DEFAULT_RANDOM_WEIGHT = .5d;

    /**
     * &alpha;, the distribution width parameter.
     */
    private final double alpha;

    /**
     * Vector count.
     */
    private final int count;

    /**
     * Weight for the best candidate.
     */
    private WeightPolicy multinomialWeightPolicy;

    /**
     * Weight policy for the random candidates.
     */
    private WeightPolicy randomWeightPolicy;

    /**
     * Constructor.
     * <p>
     * Uses the default &alpha;, count, and fixed weights.
     */
    public MultinomialDifferentiationPolicy() {
        this(DEFAULT_ALPHA, DEFAULT_COUNT, new FixedWeightPolicy(DEFAULT_MULTINOMIAL_WEIGHT), new FixedWeightPolicy(DEFAULT_RANDOM_WEIGHT));
    }

    /**
     * Constructor.
     * 
     * @param alpha The weighting factor.
     * @param count The vector count.
     * @param multinomialWeightPolicy The weight policy for the multinomially
     *        drawn candidates.
     * @param randomWeightPolicy The weight policy for the random candidate.
     */
    public MultinomialDifferentiationPolicy(final double alpha, final int count, final WeightPolicy multinomialWeightPolicy,
            final WeightPolicy randomWeightPolicy) {
        this.alpha = alpha;
        this.count = count;
        this.multinomialWeightPolicy = multinomialWeightPolicy;
        this.randomWeightPolicy = randomWeightPolicy;
    }

    /**
     * @see com.chupacadabra.evolution.DifferentiationPolicy#differentiate(com.chupacadabra.evolution.DifferentialEvolutionState,
     *      com.chupacadabra.evolution.RandomSource, int,
     *      com.chupacadabra.evolution.pool.CandidatePool)
     */
    @Override
    public double[] differentiate(final DifferentialEvolutionState state, final RandomSource randomSource, final int parentIndex, final CandidatePool pool) {
        // grab our probabilities.
        double[] probabilities = getProbabilities(pool);

        // and determine random index.
        int randomIndex = getRandomIndexFromDistribution(probabilities, randomSource);
        Candidate multinomialCandidate = pool.getCandidate(randomIndex);

        // select random candidates.
        int total = (count * 2) + 1;
        Candidate[] random = pool.selectCandidates(randomSource, total, parentIndex, randomIndex);
        Candidate parent = pool.getCandidate(parentIndex);

        // grab weights.
        double f = multinomialWeightPolicy.getWeight(state, randomSource);
        double k = randomWeightPolicy.getWeight(state, randomSource);

        // assemble trial vector.
        double[] trial = random[0].getParameters();

        PairwiseWeightedParameterSum.computeInPlace(f, trial, 0, parent, multinomialCandidate);
        PairwiseWeightedParameterSum.computeInPlace(k, trial, 1, random);

        return trial;
    }

    /**
     * Create the probabilities from the specified pool.
     * 
     * @param pool The pool.
     * @return The multinomial probabilities for each candidate.
     */
    private double[] getProbabilities(final CandidatePool pool) {
        int size = pool.getSize();

        // find highest and lowest fitness values.
        double lowest = pool.getCandidate(0).getFitness();
        double highest = lowest;

        for (int index = 1; index < size; index++) {
            double fitness = pool.getCandidate(index).getFitness();
            lowest = Math.min(lowest, fitness);
            highest = Math.max(highest, fitness);
        }

        // assemble the probabilities.
        double[] probabilities = new double[size];
        double sum = 0;
        for (int index = 0; index < size; index++) {
            double fitness = pool.getCandidate(index).getFitness();
            double value = Math.exp((-alpha * (fitness - lowest)) / (highest - lowest));
            probabilities[index] = value;
            sum += value;
        }

        // determine scaling coefficient.
        double k = 1d / sum;
        for (int index = 0; index < size; index++) {
            probabilities[index] *= k;
        }

        return probabilities;
    }

    /**
     * Get a random index from the specified "distribution".
     * 
     * @param probabilities The probabilities.
     * @param randomSource A source of randomness.
     * @return A random index.
     */
    private int getRandomIndexFromDistribution(final double[] probabilities, final RandomSource randomSource) {
        // pick a uniform value.
        double uniform = randomSource.nextDouble();

        // and find lowest index with cumulative probability exceeding said
        // value.
        int size = probabilities.length;
        double sum = 0;
        for (int randomIndex = 0; randomIndex < size; randomIndex++) {
            sum += probabilities[randomIndex];
            if (sum >= uniform) {
                return randomIndex;
            }
        }

        // very unlikely... but possible!
        return size;
    }

}
