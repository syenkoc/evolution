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

import com.chupacadabra.evolution.DiversityPolicy;
import com.chupacadabra.evolution.FeasibilityFunction;
import com.chupacadabra.evolution.FitnessFunction;
import com.chupacadabra.evolution.RandomParametersFunction;
import com.chupacadabra.evolution.RandomSource;
import com.chupacadabra.evolution.RecombinationPolicy;
import com.chupacadabra.evolution.ViolationFunction;
import com.chupacadabra.evolution.WeightPolicy;

/**
 * Provides threadsafe wrappers using sychronization.
 * <p>
 * This class uses the singleton design pattern.
 */
final class Synchronization implements Threadsafe {

    /**
     * The lone instance of this class.
     */
    private static final Synchronization instance = new Synchronization();

    /**
     * Get the instance of this class.
     * 
     * @return The instance.
     */
    static Synchronization getInstance() {
        return instance;
    }

    /**
     * Constructor.
     */
    private Synchronization() {
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.RandomParametersFunction)
     */
    @Override
    public RandomParametersFunction threadsafe(final RandomParametersFunction randomParametersFunction) {
        return new SynchronizedRandomParametersFunction(randomParametersFunction);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.FitnessFunction)
     */
    @Override
    public FitnessFunction threadsafe(final FitnessFunction fitnessFunction) {
        return new SynchronizedFitnessFunction(fitnessFunction);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.FeasibilityFunction)
     */
    @Override
    public FeasibilityFunction threadsafe(final FeasibilityFunction feasibilityFunction) {
        return new SynchronizedFeasibilityFunction(feasibilityFunction);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.ViolationFunction)
     */
    @Override
    public ViolationFunction threadsafe(final ViolationFunction violationFunction) {
        return new SynchronizedViolationFunction(violationFunction);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.RandomSource)
     */
    @Override
    public RandomSource threadsafe(final RandomSource randomSource) {
        return new SynchronizedRandomSource(randomSource);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.DiversityPolicy)
     */
    @Override
    public DiversityPolicy threadsafe(final DiversityPolicy diversityPolicy) {
        return new SynchronizedDiversityPolicy(diversityPolicy);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.RecombinationPolicy)
     */
    @Override
    public RecombinationPolicy threadsafe(final RecombinationPolicy recombinationPolicy) {
        return new SynchronizedRecombinationPolicy(recombinationPolicy);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.WeightPolicy)
     */
    @Override
    public WeightPolicy threadsafe(final WeightPolicy weightPolicy) {
        return new SynchronizedWeightPolicy(weightPolicy);
    }

}
