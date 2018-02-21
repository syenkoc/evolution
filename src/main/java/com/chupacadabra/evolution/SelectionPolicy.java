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

import java.util.List;

/**
 * Selection policy.
 */
@FunctionalInterface
public interface SelectionPolicy {

    /**
     * Select the better candidate.
     * 
     * @param state The current state.
     * @param randomSource A source of randomness.
     * @param a First candidate.
     * @param b Second candidate.
     * @return The better candidate.
     */
    public Candidate select(DifferentialEvolutionState state, RandomSource randomSource, Candidate a, Candidate b);

    /**
     * Select the best candidate.
     * 
     * @param state The state.
     * @param randomSource Source of randomness.
     * @param candidates Candidate list.
     * @return The best candidate.
     */
    public default Candidate select(final DifferentialEvolutionState state, final RandomSource randomSource, final List<Candidate> candidates) {
        Candidate bestChild = candidates.get(0);

        // repeatedly apply the selection policy.
        for (int index = 1; index < candidates.size(); index++) {
            bestChild = select(state, randomSource, bestChild, candidates.get(index));
        }

        return bestChild;
    }

}
