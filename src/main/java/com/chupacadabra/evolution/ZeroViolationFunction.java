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
 * The zero violation function.
 * <p>
 * This class uses the singleton design pattern.
 */
public final class ZeroViolationFunction implements ViolationFunction {

    /**
     * The lone instance.
     */
    private static final ZeroViolationFunction instance = new ZeroViolationFunction();

    /**
     * Get the instance of this class.
     * 
     * @return The instance.
     */
    public static ZeroViolationFunction getInstance() {
        return instance;
    }

    /**
     * Constructor.
     */
    private ZeroViolationFunction() {
    }

    /**
     * @see com.chupacadabra.evolution.ViolationFunction#getViolation(double[])
     */
    @Override
    public double getViolation(final double[] candidate) {
        return 0;
    }

}
