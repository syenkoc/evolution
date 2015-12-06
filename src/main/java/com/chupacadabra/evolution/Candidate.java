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
 * A candidate.
 * <p>
 * This is really just a tuple of a parameter vector, fitness measure, and
 * violation measure (if the candidate is invalid).
 */
public final class Candidate
{

	/**
	 * Create a feasible candidate.
	 * 
	 * @param parameters The parameters.
	 * @param fitness The fitness.
	 * @return A feasible candidate.
	 */
	public static Candidate feasible(final double[] parameters,
			final double fitness)
	{
		return new Candidate(parameters, fitness,
				null);
	}
	
	/**
	 * Create a violating candidate.
	 * 
	 * @param parameters The parameters.
	 * @param fitness The fitness.
	 * @param violation The violation.
	 * @return A violating candidate.
	 */
	public static Candidate violating(final double[] parameters,
			final double fitness, final double violation)
	{
		return new Candidate(parameters, fitness, violation);
	}

	/**
	 * The parameter vector.
	 */
	private final double[] parameters;

	/**
	 * The fitness measure.
	 */
	private final double fitness;

	/**
	 * The violation; or <code>null</code>.
	 */
	private final Double violation;

	/**
	 * Constructor.
	 * 
	 * @param parameters The parameters.
	 * @param fitness The fitness.
	 * @param violation The violation measure; or <code>null</code>
	 */
	private Candidate(final double[] parameters, final double fitness,
			final Double violation)
	{
		this.parameters = parameters.clone();
		this.fitness = fitness;
		this.violation = violation;
	}

	/**
	 * Is this candidate feasible?
	 * 
	 * @return <code>true</code> if feasible; and <code>false</code> otherwise.
	 */
	public boolean isFeasible()
	{
		return (violation == null);
	}

	/**
	 * Is this candidate is in violating?
	 * 
	 * @return <code>true</code> if this candidate is in violation; and
	 *         <code>false</code> otherwise.
	 */
	public boolean isViolating()
	{
		return (isFeasible() == false);
	}

	/**
	 * Get the parameters.
	 * <p>
	 * The returned vector is a clone and so can be modified in place without
	 * affecting <code>this</code>.
	 * 
	 * @return The parameters The parameters.
	 */
	public double[] getParameters()
	{
		return parameters.clone();
	}
	
	/**
	 * Get the parameters without making a clone.
	 * 
	 * @return The parameters.
	 */
	double[] getParametersReference() 
	{
		return parameters;
	}

	/**
	 * Get the fitness measure.
	 * 
	 * @return The fitness measure.
	 */
	public double getFitness()
	{
		return fitness;
	}

	/**
	 * Get the violation measure.
	 * <p>
	 * This method can only be called if this candidate is
	 * {@linkplain #isViolating() in violation}.
	 * 
	 * @return The violation
	 */
	public double getViolation()
	{
		return violation;
	}

}
