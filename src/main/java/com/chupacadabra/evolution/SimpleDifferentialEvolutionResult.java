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
 * Simple mutable result implementation.
 */
public final class SimpleDifferentialEvolutionResult implements DifferentialEvolutionResult {

    /**
     * The best candidate.
     */
    private Candidate bestCandidate;

    /**
     * Termination reason.
     */
    private TerminationReason terminationReason;

    /**
     * Time taken.
     */
    private TimeLength timeTaken;

    /**
     * @return The bestCandidate
     */
    @Override
    public Candidate getBestCandidate() {
        return bestCandidate;
    }

    /**
     * @param bestCandidate The value.
     */
    public void setBestCandidate(Candidate bestCandidate) {
        this.bestCandidate = bestCandidate;
    }

    /**
     * @return The terminationReason
     */
    @Override
    public TerminationReason getTerminationReason() {
        return terminationReason;
    }

    /**
     * @param terminationReason The value.
     */
    public void setTerminationReason(TerminationReason terminationReason) {
        this.terminationReason = terminationReason;
    }

    /**
     * @return The timeTaken
     */
    @Override
    public TimeLength getTimeTaken() {
        return timeTaken;
    }

    /**
     * @param timeTaken The value.
     */
    public void setTimeTaken(TimeLength timeTaken) {
        this.timeTaken = timeTaken;
    }

}
