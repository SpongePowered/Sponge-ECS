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

import java.util.BitSet;

public class ComponentManager extends Manager {
    private Bag<Bag<Component>> componentsByType;
    private Bag<Entity> deleted;

    public ComponentManager() {
        componentsByType = new Bag<Bag<Component>>();
        deleted = new Bag<Entity>();
    }

    @Override
    protected void initialize() {
    }

    private void removeComponentsOfEntity(Entity e) {
        BitSet componentBits = e.getComponentBits();
        for (int i = componentBits.nextSetBit(0); i >= 0; i = componentBits.nextSetBit(i + 1)) {
            componentsByType.get(i).set(e.getId(), null);
        }
        componentBits.clear();
    }

    protected void addComponent(Entity e, ComponentType type, Component component) {
        componentsByType.ensureCapacity(type.getIndex());

        Bag<Component> components = componentsByType.get(type.getIndex());
        if (components == null) {
            components = new Bag<Component>();
            componentsByType.set(type.getIndex(), components);
        }

        components.set(e.getId(), component);

        e.getComponentBits().set(type.getIndex());
    }

    protected void removeComponent(Entity e, ComponentType type) {
        if (e.getComponentBits().get(type.getIndex())) {
            componentsByType.get(type.getIndex()).set(e.getId(), null);
            e.getComponentBits().clear(type.getIndex());
        }
    }

    protected Bag<Component> getComponentsByType(ComponentType type) {
        Bag<Component> components = componentsByType.get(type.getIndex());
        if (components == null) {
            components = new Bag<Component>();
            componentsByType.set(type.getIndex(), components);
        }
        return components;
    }

    protected Component getComponent(Entity e, ComponentType type) {
        Bag<Component> components = componentsByType.get(type.getIndex());
        if (components != null) {
            return components.get(e.getId());
        }
        return null;
    }

    public Bag<Component> getComponentsFor(Entity e, Bag<Component> fillBag) {
        BitSet componentBits = e.getComponentBits();

        for (int i = componentBits.nextSetBit(0); i >= 0; i = componentBits.nextSetBit(i + 1)) {
            fillBag.add(componentsByType.get(i).get(e.getId()));
        }

        return fillBag;
    }

    @Override
    public void deleted(Entity e) {
        deleted.add(e);
    }

    protected void clean() {
        if (deleted.size() > 0) {
            for (int i = 0; deleted.size() > i; i++) {
                removeComponentsOfEntity(deleted.get(i));
            }
            deleted.clear();
        }
    }
}
