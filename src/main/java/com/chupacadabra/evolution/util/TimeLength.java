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
package com.chupacadabra.evolution.util;

import java.util.concurrent.TimeUnit;

/**
 * A length of time.
 */
public final class TimeLength implements Comparable<TimeLength>
{

	/**
	 * Value.
	 */
	private final long value;

	/**
	 * Unit.
	 */
	private final TimeUnit timeUnit;

	/**
	 * Constructor.
	 * 
	 * @param value The value.
	 * @param timeUnit The unit.
	 */
	public TimeLength(final long value, final TimeUnit timeUnit)
	{
		this.value = value;
		this.timeUnit = timeUnit;
	}

	/**
	 * Get the value in the specified unit.
	 * 
	 * @param unit The desired time unit
	 * @return The value
	 */
	public long getValue(final TimeUnit unit)
	{
		return unit.convert(value, timeUnit);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final TimeLength that)
	{
		return Long.compare(getValue(TimeUnit.NANOSECONDS), that.getValue(TimeUnit.NANOSECONDS)); 
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TimeLength [value=");
		builder.append(value);
		builder.append(", timeUnit=");
		builder.append(timeUnit);
		builder.append(" (");
		builder.append(getValue(TimeUnit.MILLISECONDS));
		builder.append(" millis)");
		builder.append("]");
		return builder.toString();
	}
	
	

}
