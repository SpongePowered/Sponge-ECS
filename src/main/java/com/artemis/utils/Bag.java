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

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Collection type a bit like ArrayList but does not preserve the order of its
 * entities, speedwise it is very good, especially suited for games.
 * <p/>
 * For speed this only check for identical objects and it does not invoke {@link java.lang.Object#equals(Object)}.
 */

public class Bag<E> implements ImmutableBag<E> {
    static final int DEFAULT_CAPACITY = 64;

    private E[] data;
    private int size = 0;

    /**
     * Constructs an empty Bag with an initial capacity of 64.
     */
    public Bag() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs an empty Bag with the specified initial capacity.
     *
     * @param capacity the initial capacity of Bag
     */
    @SuppressWarnings("unchecked")
    public Bag(int capacity) {
        data = (E[]) new Object[capacity];
    }

    /**
     * Removes the element at the specified position in this Bag. does this by
     * overwriting it was last element then removing last element
     *
     * @param index the index of element to be removed
     * @return element that was removed from the Bag
     */
    public E remove(int index) {
        E e = data[index]; // make copy of element to remove so it can be returned
        data[index] = data[--size]; // overwrite item to remove with last element
        data[size] = null; // null last element, so gc can do its work
        return e;
    }

    /**
     * Remove and return the last object in the bag.
     *
     * @return the last object in the bag, null if empty.
     */
    @Nullable
    public E removeLast() {
        if (size > 0) {
            E e = data[--size];
            data[size] = null;
            return e;
        }

        return null;
    }

    /**
     * Removes the first occurrence of the specified element from this Bag, if
     * it is present. If the Bag does not contain the element, it is unchanged.
     * does this by overwriting it was last element then removing last element
     *
     * @param e element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean remove(E e) {
        for (int i = 0; i < size; i++) {
            E e2 = data[i];

            if (e == e2) {
                data[i] = data[--size]; // overwrite item to remove with last element
                data[size] = null; // null last element, so gc can do its work
                return true;
            }
        }

        return false;
    }

    /**
     * Check if bag contains this element.
     *
     * @param e The element
     * @return
     */
    @Override
    public boolean contains(E e) {
        for (int i = 0; i < size; i++) {
            if (e == data[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes from this Bag all of its elements that are contained in the
     * specified other Bag.
     *
     * @param bag other bag containing elements to be removed from this Bag
     * @return {@code true} if this Bag changed as a result of the call
     */
    public boolean removeAll(ImmutableBag<E> bag) {
        if (bag == this) {
            throw new IllegalArgumentException("removeAll doesn't support from itself to itself");
        }

        boolean modified = false;
        for (int i = 0; i < bag.size(); i++) {
            E e1 = bag.get(i);

            for (int j = 0; j < size; j++) {
                E e2 = data[j];

                if (e1 == e2) {
                    remove(j);
                    j--;
                    modified = true;
                    break;
                }
            }
        }

        return modified;
    }

    /**
     * Returns the element at the specified position in Bag.
     *
     * @param index index of the element to return
     * @return the element at the specified position in bag
     */
    @Override
    public E get(int index) {
        return data[index];
    }

    /**
     * Returns the number of elements in this bag.
     *
     * @return the number of elements in this bag
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the number of elements the bag can hold without growing.
     *
     * @return the number of elements the bag can hold without growing.
     */
    public int getCapacity() {
        return data.length;
    }

    /**
     * Checks if the internal storage supports this index.
     *
     * @param index
     * @return
     */
    public boolean isIndexWithinBounds(int index) {
        return index >= 0 && index < getCapacity();
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds the specified element to the end of this bag. if needed also
     * increases the capacity of the bag.
     *
     * @param e element to be added to this list
     */
    public void add(E e) {
        // is size greater than capacity increase capacity
        if (size == data.length) {
            grow(size + 1);
        }

        data[size++] = e;
    }

    /**
     * Set element at specified index in the bag.
     *
     * @param index position of element
     * @param e     the element
     */
    public void set(int index, E e) {
        if (index >= data.length) {
            grow(index * 2);
        }
        size = index + 1;
        data[index] = e;
    }

    private void grow(int newCapacity) {
        data = Arrays.copyOf(data, Math.max(newCapacity, (data.length * 3) / 2 + 1));
    }

    public void ensureCapacity(int index) {
        if (index >= data.length) {
            grow(index + 1);
        }
    }

    /**
     * Removes all of the elements from this bag. The bag will be empty after
     * this call returns.
     */
    public void clear() {
        // null all elements so gc can clean up
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }

        size = 0;
    }

    /**
     * Add all items from another bag into this bag.
     *
     * @param bag the other bag to add
     */
    public void addAll(ImmutableBag<E> bag) {
        if (bag == this) {
            throw new IllegalArgumentException("addAll doesn't support from itself to itself");
        }

        for (int i = 0; bag.size() > i; i++) {
            add(bag.get(i));
        }
    }
}
