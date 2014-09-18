/**
 * This file is part of Artemis, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 SpongePowered <http://spongepowered.org/>
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.artemis.utils;

public class FastMath {
	public static final double PI = Math.PI;
	public static final double SQUARED_PI = PI * PI;
	public static final double HALF_PI = 0.5 * PI;
	public static final double TWO_PI = 2.0 * PI;
	public static final double THREE_PI_HALVES = TWO_PI - HALF_PI;

	private static final double _sin_a = -4 / SQUARED_PI;
	private static final double _sin_b = 4 / PI;
	private static final double _sin_p = 9d / 40;

	private final static double _asin_a = -0.0481295276831013447d;
	private final static double _asin_b = -0.343835993947915197d;
	private final static double _asin_c = 0.962761848425913169d;
	private final static double _asin_d = 1.00138940860107040d;

	private final static double _atan_a = 0.280872d;

	public final static double cos(final double x) {
		return sin(x + ((x > HALF_PI) ? -THREE_PI_HALVES : HALF_PI));
	}

	public final static double sin(double x) {
		x = _sin_a * x * Math.abs(x) + _sin_b * x;
		return _sin_p * (x * Math.abs(x) - x) + x;
	}

	public final static double tan(final double x) {
		return sin(x) / cos(x);
	}

	public final static double asin(final double x) {
		return x * (Math.abs(x) * (Math.abs(x) * _asin_a + _asin_b) + _asin_c) + Math.signum(x) * (_asin_d - Math.sqrt(1 - x * x));
	}

	public final static double acos(final double x) {
		return HALF_PI - asin(x);
	}

	public final static double atan(final double x) {
		return (Math.abs(x) < 1) ? x / (1 + _atan_a * x * x) : Math.signum(x) * HALF_PI - x / (x * x + _atan_a);
	}

	public final static double inverseSqrt(double x) {
		final double xhalves = 0.5d * x;
		x = Double.longBitsToDouble(0x5FE6EB50C7B537AAl - (Double.doubleToRawLongBits(x) >> 1));
		return x * (1.5d - xhalves * x * x); // more iterations possible
	}

	public final static double sqrt(final double x) {
		return x * inverseSqrt(x);
	}

}
