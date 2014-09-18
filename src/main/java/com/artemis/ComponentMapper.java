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

import com.artemis.utils.Bag;

/**
 * High performance component retrieval from entities. Use this wherever you
 * need to retrieve components from entities often and fast.
 * 
 * @author Arni Arent
 *
 * @param <A> the class type of the component
 */
public class ComponentMapper<A extends Component> {
	private ComponentType type;
	private Class<A> classType;
	private Bag<Component> components;

	private ComponentMapper(Class<A> type, World world) {
		this.type = ComponentType.getTypeFor(type);
		components = world.getComponentManager().getComponentsByType(this.type);
		this.classType = type;
	}

	/**
	 * Fast but unsafe retrieval of a component for this entity.
	 * No bounding checks, so this could throw an ArrayIndexOutOfBoundsExeption,
	 * however in most scenarios you already know the entity possesses this component.
	 * 
	 * @param e the entity that should possess the component
	 * @return the instance of the component
	 */
	public A get(Entity e) {
		return classType.cast(components.get(e.getId()));
	}

	/**
	 * Fast and safe retrieval of a component for this entity.
	 * If the entity does not have this component then null is returned.
	 * 
	 * @param e the entity that should possess the component
	 * @return the instance of the component
	 */
	public A getSafe(Entity e) {
		if(components.isIndexWithinBounds(e.getId())) {
			return classType.cast(components.get(e.getId()));
		}
		return null;
	}
	
	/**
	 * Checks if the entity has this type of component.
	 * @param e the entity to check
	 * @return true if the entity has this component type, false if it doesn't.
	 */
	public boolean has(Entity e) {
		return getSafe(e) != null;		
	}

	/**
	 * Returns a component mapper for this type of components.
	 * 
	 * @param type the type of components this mapper uses.
	 * @param world the world that this component mapper should use.
	 * @return a new mapper.
	 */
	public static <T extends Component> ComponentMapper<T> getFor(Class<T> type, World world) {
		return new ComponentMapper<T>(type, world);
	}

}
