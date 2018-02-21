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
 * Termination due to a user-defined criterion being met.
 */
public final class TerminationCriterionMet implements TerminationReason {

    /**
     * The criterion that caused us to terminate.
     */
    private final TerminationCriterion terminationCriterion;

    /**
     * Constructor.
     * 
     * @param terminationCriterion The termination criterion.
     */
    public TerminationCriterionMet(final TerminationCriterion terminationCriterion) {
        this.terminationCriterion = terminationCriterion;
    }

    /**
     * Get the criterion that caused this optimization to terminate.
     * 
     * @return Said criterion.
     */
    public TerminationCriterion getTerminationCriterion() {
        return terminationCriterion;
    }

    /**
     * @see com.chupacadabra.evolution.TerminationReason#accept(com.chupacadabra.evolution.TerminationReasonVisitor,
     *      java.lang.Object)
     */
    @Override
    public <TPayload, TReturn> TReturn accept(final TerminationReasonVisitor<TPayload, TReturn> visitor, final TPayload payload) {
        return visitor.visit(this, payload);
    }

}
