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
package com.chupacadabra.evolution.simple;

import com.chupacadabra.evolution.DifferentialEvolutionOptimizer;
import com.chupacadabra.evolution.DifferentialEvolutionResult;
import com.chupacadabra.evolution.FitnessFunction;
import com.chupacadabra.evolution.NOrthotopeRandomParametersFunction;
import com.chupacadabra.evolution.SerialDifferentialEvolutionOptimizer;
import com.chupacadabra.evolution.SimpleDifferentialEvolutionProblem;

/**
 * A simple example.
 */
public final class SimpleExample {

    public static void main(String[] args) {
        // construct our function.
        FitnessFunction fitnessFunction = (double[] p) -> (p[0] * p[0] / 10) + (4 * Math.sin(p[0]));

        // this will create uniformly distributed candidates on (-10, 10)
        NOrthotopeRandomParametersFunction parameterFunction = new NOrthotopeRandomParametersFunction(1);
        parameterFunction.setParameterRange(0, -10, 10);

        // now, set up a problem.
        SimpleDifferentialEvolutionProblem problem = new SimpleDifferentialEvolutionProblem();
        problem.setDimension(1);
        problem.setRandomParametersFunction(parameterFunction);
        problem.setFitnessFunction(fitnessFunction);

        // create a suitable optimizer.
        DifferentialEvolutionOptimizer optimizer = new SerialDifferentialEvolutionOptimizer();

        // now just get the result!
        DifferentialEvolutionResult result = optimizer.optimize(problem);

        // extract x and f(x).
        double x = result.getBestCandidate().getParameters()[0];
        double fx = result.getBestCandidate().getFitness();

        System.out.println(x);
        System.out.println(fx);
        System.out.println(result.getTimeTaken());
    }

}
