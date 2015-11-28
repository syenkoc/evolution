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
 * Deb's selection rule.
 * <p>
 * These rules are:
 * <ol>
 *  <li>If both candidates are in violation, select the candidate with the lower
 *      violation value.</li>
 *  <li>If one candidates is in violation but the other is not, select the
 *      non-violating candidate.</li>
 *  <li>If neither candidate is in violation, select the candidate with the
 *      better (lower) fitness.</li>
 * </ol>
 */
public final class DebSelectionPolicy implements SelectionPolicy
{

	/**
	 * @see com.chupacadabra.evolution.SelectionPolicy#select(DifferentialEvolutionState, RandomSource, com.chupacadabra.evolution.Candidate, com.chupacadabra.evolution.Candidate)
	 */
	@Override
	public Candidate select(DifferentialEvolutionState state, RandomSource randomSource, final Candidate a, final Candidate b)
	{
		if(a.isViolating() && b.isViolating())
		{
			// both are violating, choose candidate with lower violation.
			return (a.getViolation() < b.getViolation()) ? a : b;
		}

		if(a.isFeasible() && b.isFeasible())
		{
			// both are non-violating, choose function with better fitness.
			return (a.getFitness() < b.getFitness()) ? a : b;
		}

		// select non-violating if we have mixed violation parity.
		return a.isFeasible() ? a : b;
	}

}
