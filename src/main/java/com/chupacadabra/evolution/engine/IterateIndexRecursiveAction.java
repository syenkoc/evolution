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

import java.util.concurrent.RecursiveAction;

import com.chupacadabra.evolution.Candidate;

/**
 * Index iteration as a recursive action.
 */
public final class IterateIndexRecursiveAction extends RecursiveAction {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The receiver.
     */
    private final DifferentialEvolutionReceiver optimizer;

    /**
     * The index.
     */
    private final int index;

    /**
     * Child generation strategy.
     */
    private final ChildGeneration childGeneration;

    /**
     * Constructor.
     * 
     * @param optimizer The receiver.s
     * @param index The index.
     * @param childGeneration Child generation strategy.
     */
    public IterateIndexRecursiveAction(final DifferentialEvolutionReceiver optimizer, final int index, final ChildGeneration childGeneration) {
        this.optimizer = optimizer;
        this.index = index;
        this.childGeneration = childGeneration;
    }

    /**
     * @see java.util.concurrent.RecursiveAction#compute()
     */
    @Override
    protected void compute() {
        // just run the standard iteration command.
        Candidate parent = getParent();
        IterateIndexAction command = new IterateIndexAction(optimizer, index, parent, childGeneration);
        command.run();
    }

    /**
     * Get the parent.
     * 
     * @return The parent.
     */
    private Candidate getParent() {
        optimizer.getPoolLock().lock(PoolType.CURRENT, LockType.READ);
        try {
            return optimizer.getCurrentPool().getCandidate(index);
        } finally {
            optimizer.getPoolLock().unlock(PoolType.CURRENT, LockType.READ);
        }
    }

}
