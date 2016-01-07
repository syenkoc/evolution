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
import com.chupacadabra.evolution.threadsafe.Threadsafe;

/**
 * A differentiation policy.
 * <p>
 * The differentiation policy generates <i>trial</i> parameter vectors that are
 * then <i>recombined</i> (using the {@linkplain RecombinationPolicy
 * recombination policy}) to generate <i>child</i> vectors.
 * <p>
 * Note that the state of the candidate pool will be <i>invariant</i> -
 * essentially read-locked - during the execution of the differentiation step.
 * This allows you to make multiple calls to the pool knowing that its state
 * will not change.
 * <p>
 * Implementations of this interface <i>must</i> be safe for use by multiple
 * threads if they are used in the parallel optimizer. You can obtain a
 * threadsafe decorator around any implementation of this interface using the
 * {@link Threadsafe} framework.
 */
@FunctionalInterface
public interface DifferentiationPolicy
{

	/**
	 * Perform parameter differentiation.
	 * 
	 * @param state The state.
	 * @param randomSource A source of randomness.
	 * @param parentIndex The index of the parent in the pool.
	 * @param pool The pool.
	 * @return The trial or differentiated parameters.
	 */
	public double[] differentiate(DifferentialEvolutionState state,
			RandomSource randomSource, int parentIndex, CandidatePool pool);

}
