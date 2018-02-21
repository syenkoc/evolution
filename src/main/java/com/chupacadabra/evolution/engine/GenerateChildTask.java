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
package com.chupacadabra.evolution.engine;

import java.util.concurrent.Callable;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.DifferentiationPolicy;
import com.chupacadabra.evolution.FeasibilityFunction;
import com.chupacadabra.evolution.FeasibilityType;
import com.chupacadabra.evolution.FitnessFunction;
import com.chupacadabra.evolution.RandomSource;
import com.chupacadabra.evolution.RecombinationPolicy;
import com.chupacadabra.evolution.ViolationFunction;
import com.chupacadabra.evolution.pool.CandidatePool;

/**
 * Core child generation algorithm.
 */
public final class GenerateChildTask implements Callable<Candidate> {

    /**
     * The receiver.
     */
    private final DifferentialEvolutionReceiver optimizer;

    /**
     * The parent index.
     */
    private final int index;

    /**
     * The parent.
     */
    private final Candidate parent;

    /**
     * Constructor.
     * 
     * @param optimizer The receiver.
     * @param index The parent index.
     * @param parent The parent candidate.
     */
    public GenerateChildTask(final DifferentialEvolutionReceiver optimizer, final int index, final Candidate parent) {
        this.optimizer = optimizer;
        this.index = index;
        this.parent = parent;
    }

    /**
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public Candidate call() {
        // grab parent and trial parameters.
        double[] parentParameters = parent.getParameters();
        double[] trialParameters = generateTrial();

        // perform recombination to get child parameters.
        DifferentialEvolutionSettings settings = optimizer.getSettings();
        RandomSource randomSource = settings.getRandomSource();
        RecombinationPolicy recombinationPolicy = settings.getRecombinationPolicy();
        double[] child = recombinationPolicy.recombine(optimizer, randomSource, parentParameters, trialParameters);

        // classify the child parameters.
        DifferentialEvolutionProblem problem = optimizer.getProblem();
        FeasibilityFunction feasibilityFunction = problem.getFeasibilityFunction();
        FeasibilityType feasibility = feasibilityFunction.getFeasibilityType(child);

        if (feasibility == FeasibilityType.INFEASIBLE) {
            // we can short-circuit fitness calculation.
            return null;
        }

        // measure fitness of child parameters.
        FitnessFunction fitnessFunction = problem.getFitnessFunction();
        double childFitness = fitnessFunction.getFitness(child);

        switch (feasibility) {
            case FEASIBLE:
                return Candidate.feasible(child, childFitness);
            case VIOLATING:
                // in this case we also have to measure the violation.
                ViolationFunction violationFunction = problem.getViolationFunction();
                double violation = violationFunction.getViolation(child);

                return Candidate.violating(child, childFitness, violation);
            case INFEASIBLE:
            default:
                // impossible.
                throw new InternalError();
        }
    }

    /**
     * Generate trial parameters using differentiation.
     * 
     * @return Trial parameters.
     */
    private double[] generateTrial() {
        DifferentialEvolutionSettings settings = optimizer.getSettings();
        RandomSource randomSource = settings.getRandomSource();
        DifferentiationPolicy diffentiationPolicy = settings.getDifferentiationPolicy();
        CandidatePool currentPool = optimizer.getCurrentPool();

        // read-lock the pool while we differentiate. This way, the
        // differentiation policy can make several calls to the pool knowing
        // that the state won't change.
        optimizer.getPoolLock().lock(PoolType.CURRENT, LockType.READ);
        try {
            double[] trialParameters = diffentiationPolicy.differentiate(optimizer, randomSource, index, currentPool);

            return trialParameters;
        } finally {
            optimizer.getPoolLock().unlock(PoolType.CURRENT, LockType.READ);
        }
    }

}
