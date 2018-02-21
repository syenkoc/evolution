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

import com.chupacadabra.evolution.util.TimeLength;

/**
 * Maximum time termination criterion.
 */
public final class MaximumTime implements TerminationCriterion {

    /**
     * The maximum time.
     */
    private final TimeLength maximumTime;

    /**
     * Constructor.
     * 
     * @param maximumTime The maximum time.
     */
    public MaximumTime(final TimeLength maximumTime) {
        this.maximumTime = maximumTime;
    }

    /**
     * @see com.chupacadabra.evolution.TerminationCriterion#isMet(com.chupacadabra.evolution.DifferentialEvolutionState)
     */
    @Override
    public boolean isMet(final DifferentialEvolutionState state) {
        return maximumTime.compareTo(state.getTimeTaken()) > 0;
    }

}
