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

/**
 * 
 */
public class RosenbrockFunctionTest {

    public static void main(String[] args) throws Exception {

        ForkJoinPool pool = new ForkJoinPool(4);
        ForkJoinDifferentialEvolutionOptimizerConfiguration configuration = new ForkJoinDifferentialEvolutionOptimizerConfiguration();
        configuration.setInitializationThreshold(32);

        DifferentialEvolutionOptimizer optimizer1 = new SerialDifferentialEvolutionOptimizer();
        DifferentialEvolutionOptimizer optimizer2 = new ForkJoinDifferentialEvolutionOptimizer(pool, configuration);

        solve(optimizer1, true);
        solve(optimizer2, true);

        System.exit(0);

        System.in.read();

        for (int index = 0; index < 10; index++) {
            solve(optimizer1, false);
        }

        System.out.println("Done 1");
        System.in.read();

        for (int index = 0; index < 10; index++) {
            solve(optimizer2, false);
        }

        System.out.println("Done 2");
    }

    static void solve(final DifferentialEvolutionOptimizer optimizer, boolean print) {
        RosenbrockFunctionProblem problem = new RosenbrockFunctionProblem(new RosenbrockFunction(1, 100));
        DifferentialEvolutionSettings settings = new DifferentialEvolutionSettings();
        settings.setMaximumGeneration(10000);
        settings.setRandomSource(new JavaUtilRandomSource(1));
        settings.setChildrenPerCandidate(7);
        settings.setCandidatePoolSize(80);
        settings.setPoolReplacement(PoolReplacement.IMMEDIATELY);

        // now just get the result!
        DifferentialEvolutionResult result = optimizer.optimize(problem, settings);

        // extract x and f(x).
        double[] x = result.getBestCandidate().getParameters();
        double fx = result.getBestCandidate().getFitness();

        if (print) {
            System.out.println(x[0] + " " + x[1]);
            System.out.println(fx);
            System.out.println(result.getTimeTaken());
        }

    }

}
