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

public abstract class Timer {

	private float delay;
	private boolean repeat;
	private float acc;
	private boolean done;
	private boolean stopped;
	
	public Timer(float delay) {
		this(delay, false);
	}

	public Timer(float delay, boolean repeat) {
		this.delay = delay;
		this.repeat = repeat;
		this.acc = 0;
	}

	public void update(float delta) {
		if (!done && !stopped) {
			acc += delta;

			if (acc >= delay) {
				acc -= delay;

				if (repeat) {
					reset();
				} else {
					done = true;
				}

				execute();
			}
		}
	}

	public void reset() {
		stopped = false;
		done = false;
		acc = 0;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isRunning() {
		return !done && acc < delay && !stopped;
	}

	public void stop() {
		stopped = true;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public abstract void execute();

	public float getPercentageRemaining() {
		if (done)
			return 100;
		else if (stopped)
			return 0;
		else
			return 1 - (delay - acc) / delay;
	}

	public float getDelay() {
		return delay;
	}

}
