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

import com.chupacadabra.evolution.threadsafe.Threadsafe;

/**
 * A parameter vector recombination policy.
 * <p>
 * This policy recombines a parent parameter vector and a trial vector
 * {@linkplain DifferentiationPolicy differentiated} from it to produce a
 * <i>child</i> parameter vector.
 * <p>
 * Implementations of this interface <i>must</i> be safe for use by multiple
 * threads if they are used in the parallel optimizer. You can obtain a
 * threadsafe decorator around any implementation of this interface using the
 * {@link Threadsafe} framework.
 */
@FunctionalInterface
public interface RecombinationPolicy {

    /**
     * Recombine the specified trials vectors.
     * 
     * @param parent Parent vector.
     * @param trial The trial vector differentiated from the parent.
     * @param state Current state.
     * @param randomSource A source of randomness.
     * @return Recombined vector.
     */
    public double[] recombine(DifferentialEvolutionState state, RandomSource randomSource, double[] parent, double[] trial);

}
