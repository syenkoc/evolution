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
import com.chupacadabra.evolution.LockFairness;
import com.chupacadabra.evolution.RandomParametersFunction;
import com.chupacadabra.evolution.RandomSource;
import com.chupacadabra.evolution.RecombinationPolicy;
import com.chupacadabra.evolution.ViolationFunction;
import com.chupacadabra.evolution.WeightPolicy;

/**
 * Threadsafe decorator provider strategy.
 * <p>
 * Unless otherwise noted, all methods throw a {@link NullPointerException} if
 * the target object is <code>null</code>.
 */
public interface Threadsafe {

    /**
     * Get a decorator provider that ensures threadsafety using synchronization.
     * 
     * @return A provider of the aforementioned nature.
     */
    public static Threadsafe synchronization() {
        return Synchronization.getInstance();
    }

    /**
     * Get a decorator provider that ensures threadsafety using unfair locking.
     * 
     * @return A locking threadsafe provider.
     */
    public static Threadsafe locking() {
        return locking(LockFairness.UNFAIR);
    }

    /**
     * Get a decorator provider that ensures threadsafety using locking.
     * 
     * @param lockFairness The lock fairness.
     * @return A locking threadsafe provider.
     */
    public static Threadsafe locking(final LockFairness lockFairness) {
        return Locking.getInstance(lockFairness.getReentrantLockValue());
    }

    // functions.

    /**
     * Create a threadsafe wrapper around the specified function.
     * 
     * @param randomParametersFunction The function to wrap.
     * @return A threadsafe view.
     */
    public RandomParametersFunction threadsafe(RandomParametersFunction randomParametersFunction);

    /**
     * Create a threadsafe wrapper around the specified function.
     * 
     * @param fitnessFunction The function to wrap.
     * @return A threadsafe view.
     */
    public FitnessFunction threadsafe(FitnessFunction fitnessFunction);

    /**
     * Create a threadsafe wrapper around the specified function.
     * 
     * @param feasibilityFunction The function to wrap.
     * @return A threadsafe view.
     */
    public FeasibilityFunction threadsafe(FeasibilityFunction feasibilityFunction);

    /**
     * Create a threadsafe wrapper around the specified function.
     * 
     * @param violationFunction The function to wrap.
     * @return A threadsafe view.
     */
    public ViolationFunction threadsafe(ViolationFunction violationFunction);

    // policies.

    /**
     * Create a threadsafe wrapper around the specified policy.
     * 
     * @param diversityPolicy The policy to wrap.
     * @return A threadsafe view.
     */
    public DiversityPolicy threadsafe(DiversityPolicy diversityPolicy);

    /**
     * Create a threadsafe wrapper around the specified source.
     * 
     * @param randomSource The source to wrap.
     * @return A threadsafe view.
     */
    public RandomSource threadsafe(RandomSource randomSource);

    /**
     * Create a threadsafe wrapper around the specified policy.
     * 
     * @param recombinationPolicy The policy to wrap.
     * @return A threadsafe view.
     */
    public RecombinationPolicy threadsafe(RecombinationPolicy recombinationPolicy);

    /**
     * Create a threadsafe wrapper around the specified policy.
     * 
     * @param weightPolicy The policy to wrap.
     * @return A threadsafe view.
     */
    public WeightPolicy threadsafe(WeightPolicy weightPolicy);

}
