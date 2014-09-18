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
package com.artemis;

import java.util.HashMap;

public class ComponentType {
	private static int INDEX = 0;

	private final int index;
	private final Class<? extends Component> type;

	private ComponentType(Class<? extends Component> type) {
		index = INDEX++;
		this.type = type;
	}

	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		return "ComponentType["+type.getSimpleName()+"] ("+index+")";
	}

	private static HashMap<Class<? extends Component>, ComponentType> componentTypes = new HashMap<Class<? extends Component>, ComponentType>();

	public static ComponentType getTypeFor(Class<? extends Component> c) {
		ComponentType type = componentTypes.get(c);

		if (type == null) {
			type = new ComponentType(c);
			componentTypes.put(c, type);
		}

		return type;
	}

	public static int getIndexFor(Class<? extends Component> c) {
		return getTypeFor(c).getIndex();
	}
}
