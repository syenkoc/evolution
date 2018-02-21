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
 * Provides threadsafety using a re-entrant lock.
 * <p>
 * This class uses registry-of-singleton pattern.
 */
final class Locking implements Threadsafe {

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
    static Locking getInstance(final boolean fair) {
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
    private Locking(final boolean fair) {
        this.fair = fair;
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.RandomParametersFunction)
     */
    @Override
    public RandomParametersFunction threadsafe(final RandomParametersFunction randomParametersFunction) {
        return new LockedRandomParametersFunction(randomParametersFunction, fair);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.FitnessFunction)
     */
    @Override
    public FitnessFunction threadsafe(final FitnessFunction fitnessFunction) {
        return new LockedFitnessFunction(fitnessFunction, fair);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.FeasibilityFunction)
     */
    @Override
    public FeasibilityFunction threadsafe(final FeasibilityFunction feasibilityFunction) {
        return new LockedFeasibilityFunction(feasibilityFunction, fair);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.ViolationFunction)
     */
    @Override
    public ViolationFunction threadsafe(final ViolationFunction violationFunction) {
        return new LockedViolationFunction(violationFunction, fair);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.RandomSource)
     */
    @Override
    public RandomSource threadsafe(final RandomSource randomSource) {
        return new LockedRandomSource(randomSource, fair);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.DiversityPolicy)
     */
    @Override
    public DiversityPolicy threadsafe(final DiversityPolicy diversityPolicy) {
        return new LockedDiversityPolicy(diversityPolicy, fair);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.RecombinationPolicy)
     */
    @Override
    public RecombinationPolicy threadsafe(RecombinationPolicy recombinationPolicy) {
        return new LockedRecombinationPolicy(recombinationPolicy, fair);
    }

    /**
     * @see com.chupacadabra.evolution.threadsafe.Threadsafe#threadsafe(com.chupacadabra.evolution.WeightPolicy)
     */
    @Override
    public WeightPolicy threadsafe(WeightPolicy weightPolicy) {
        // TODO Auto-generated method stub
        return null;
    }

}
