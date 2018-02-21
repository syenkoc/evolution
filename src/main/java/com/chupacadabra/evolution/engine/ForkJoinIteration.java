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

import com.chupacadabra.evolution.ForkJoinDifferentialEvolutionOptimizerConfiguration;

/**
 * Iteration strategy for the fork-join pool based optimizer.
 */
public final class ForkJoinIteration implements Iteration {

    /**
     * The configuration.
     */
    private ForkJoinDifferentialEvolutionOptimizerConfiguration configuration;

    /**
     * Constructor.
     * 
     * @param configuration The configuration.
     */
    public ForkJoinIteration(final ForkJoinDifferentialEvolutionOptimizerConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @see com.chupacadabra.evolution.engine.Iteration#iterate(com.chupacadabra.evolution.engine.DifferentialEvolutionReceiver,
     *      com.chupacadabra.evolution.engine.ChildGeneration)
     */
    @Override
    public void iterate(final DifferentialEvolutionReceiver receiver, final ChildGeneration childGeneration) {
        int length = receiver.getSettings().getCandidatePoolSize();
        ForkJoinIterationRecursiveAction action = new ForkJoinIterationRecursiveAction(configuration, receiver, childGeneration, 0, length);
        action.invoke();

    }

}
