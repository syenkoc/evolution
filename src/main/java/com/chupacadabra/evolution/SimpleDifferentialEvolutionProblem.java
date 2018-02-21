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
 * A super-simple implementation of the problem interface.
 */
public class SimpleDifferentialEvolutionProblem implements DifferentialEvolutionProblem {

    /**
     * The dimension.
     */
    private int dimension;

    /**
     * Random parameter vector function.
     */
    private RandomParametersFunction randomParametersFunction;

    /**
     * Fitness function.
     */
    private FitnessFunction fitnessFunction;

    /**
     * Feasibility function.
     */
    private FeasibilityFunction feasibilityFunction;

    /**
     * The violation function.
     */
    private ViolationFunction violationFunction;

    /**
     * Constructor.
     */
    public SimpleDifferentialEvolutionProblem() {
        // use defaults.
        feasibilityFunction = AllFeasibilityFunction.getInstance();
        violationFunction = ZeroViolationFunction.getInstance();
    }

    /**
     * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getDimension()
     */
    @Override
    public int getDimension() {
        return dimension;
    }

    /**
     * Set the dimension.
     * 
     * @param dimension The dimension.
     */
    public void setDimension(final int dimension) {
        this.dimension = dimension;
    }

    /**
     * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getRandomParametersFunction()
     */
    @Override
    public RandomParametersFunction getRandomParametersFunction() {
        return randomParametersFunction;
    }

    /**
     * Set the random parameter function.
     * 
     * @param randomParametersFunction The function.
     */
    public void setRandomParametersFunction(final RandomParametersFunction randomParametersFunction) {
        this.randomParametersFunction = randomParametersFunction;
    }

    /**
     * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getFitnessFunction()
     */
    @Override
    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }

    /**
     * Set the fitness function.
     * 
     * @param fitnessFunction The fitness function.
     */
    public void setFitnessFunction(final FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    /**
     * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getFeasibilityFunction()
     */
    @Override
    public FeasibilityFunction getFeasibilityFunction() {
        return feasibilityFunction;
    }

    /**
     * Set the feasibility function.
     * 
     * @param feasibilityFunction The feasbility function.
     */
    public void setFeasibilityFunction(final FeasibilityFunction feasibilityFunction) {
        this.feasibilityFunction = feasibilityFunction;
    }

    /**
     * @see com.chupacadabra.evolution.DifferentialEvolutionProblem#getViolationFunction()
     */
    @Override
    public ViolationFunction getViolationFunction() {
        return violationFunction;
    }

    /**
     * Set the violation function.
     * 
     * @param violationFunction The violation function.
     */
    public void setViolationFunction(final ViolationFunction violationFunction) {
        this.violationFunction = violationFunction;
    }

}
