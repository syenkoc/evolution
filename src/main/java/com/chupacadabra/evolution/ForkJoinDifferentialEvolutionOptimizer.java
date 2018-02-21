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

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import com.chupacadabra.evolution.engine.ChildGeneration;
import com.chupacadabra.evolution.engine.DifferentialEvolutionEngine;
import com.chupacadabra.evolution.engine.ForkJoinChildGeneration;
import com.chupacadabra.evolution.engine.ForkJoinInitialization;
import com.chupacadabra.evolution.engine.ForkJoinIteration;
import com.chupacadabra.evolution.engine.Initialization;
import com.chupacadabra.evolution.engine.Iteration;
import com.chupacadabra.evolution.engine.PoolLock;
import com.chupacadabra.evolution.engine.PoolLockCreation;

/**
 * A differential evolution optimizer that uses a {@linkplain ForkJoinPool
 * fork-join pool} to distribute the work in parallel.
 * <p>
 * Instances of this class are safe for use by multiple threads and are also
 * safe for reentrant use.
 */
public class ForkJoinDifferentialEvolutionOptimizer implements DifferentialEvolutionOptimizer {

    /**
     * The fork-join pool to use.
     */
    private final ForkJoinPool forkJoinPool;

    /**
     * Fork-join specific configuration.
     */
    private final ForkJoinDifferentialEvolutionOptimizerConfiguration configuration;

    /**
     * Constructor.
     * <p>
     * This optimizer will use the {@linkplain ForkJoinPool#commonPool() common
     * pool}.
     */
    public ForkJoinDifferentialEvolutionOptimizer() {
        this(ForkJoinPool.commonPool(), new ForkJoinDifferentialEvolutionOptimizerConfiguration());
    }

    /**
     * Constructor.
     * 
     * @param forkJoinPool The fork-join pool.
     * @param configuration The configuration.
     */
    public ForkJoinDifferentialEvolutionOptimizer(final ForkJoinPool forkJoinPool, final ForkJoinDifferentialEvolutionOptimizerConfiguration configuration) {
        this.forkJoinPool = forkJoinPool;
        this.configuration = configuration;
    }

    /**
     * @see com.chupacadabra.evolution.DifferentialEvolutionOptimizer#optimize(com.chupacadabra.evolution.DifferentialEvolutionProblem,
     *      com.chupacadabra.evolution.DifferentialEvolutionSettings)
     */
    @Override
    public DifferentialEvolutionResult optimize(final DifferentialEvolutionProblem problem, final DifferentialEvolutionSettings settings) {
        // assemble an engine.
        PoolLockCreation lockCreation = PoolLock::reentrant;
        Initialization initialization = new ForkJoinInitialization(configuration);
        Iteration iteration = new ForkJoinIteration(configuration);
        ChildGeneration childGeneration = new ForkJoinChildGeneration(configuration);

        // build a suitable engine.
        DifferentialEvolutionEngine engine = new DifferentialEvolutionEngine(lockCreation, initialization, iteration, childGeneration);

        // and invoke core action to get us into the pool.
        ForkJoinAction action = new ForkJoinAction(engine, problem, settings);
        DifferentialEvolutionResult result = forkJoinPool.invoke(action);

        return result;
    }

    /**
     * Core fork/join action.
     */
    private static final class ForkJoinAction extends RecursiveTask<DifferentialEvolutionResult> {

        /**
         * Serial ID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The engine to use.
         */
        private final DifferentialEvolutionEngine engine;

        /**
         * The problem.
         */
        private final DifferentialEvolutionProblem problem;

        /**
         * The settings.
         */
        private final DifferentialEvolutionSettings settings;

        /**
         * Constructor.
         * 
         * @param engine The engine.
         * @param problem The problem.
         * @param settings The settings.
         */
        public ForkJoinAction(final DifferentialEvolutionEngine engine, final DifferentialEvolutionProblem problem,
                final DifferentialEvolutionSettings settings) {
            this.engine = engine;
            this.problem = problem;
            this.settings = settings;
        }

        /**
         * @see java.util.concurrent.RecursiveTask#compute()
         */
        @Override
        protected DifferentialEvolutionResult compute() {
            DifferentialEvolutionResult result = engine.getResult(problem, settings);

            return result;
        }

    }

}
