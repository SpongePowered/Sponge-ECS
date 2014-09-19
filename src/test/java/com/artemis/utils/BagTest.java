package com.artemis.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class BagTest {
    private Bag<String> subject = new Bag<String>();

    @Test
    public void getCapacity_defaultValue() {
        assertThat(subject.getCapacity(), is(Bag.DEFAULT_CAPACITY));
    }

    @Test
    public void getCapacity_defined() {
        final int capacity = 1;

        subject = new Bag<String>(capacity);

        assertThat(subject.getCapacity(), is(capacity));
    }

    @Test
    public void ensureCapacity() {
        final int capacity = 1;
        subject = new Bag<String>(capacity);

        subject.ensureCapacity(2);

        assertThat(subject.getCapacity(), is(greaterThan(capacity)));
    }

    @Test
    public void ensureCapacity_ifNeeded() {
        final int capacity = subject.getCapacity();

        subject.ensureCapacity(0);

        assertThat(subject.getCapacity(), is(capacity));
    }

    @Test
    public void isIndexWithinBounds() {
        assertThat("negative", subject.isIndexWithinBounds(-1), is(false));
        assertThat("lower", subject.isIndexWithinBounds(0), is(true));

        final int capacity = subject.getCapacity();
        assertThat("upper", subject.isIndexWithinBounds(capacity - 1), is(true));
        assertThat("above", subject.isIndexWithinBounds(capacity), is(false));
    }

    @Test
    public void empty() {
        assertThat(subject.isEmpty(), is(true));
    }

    @Test
    public void empty_misbehavesAfterSet() {
        subject.set(subject.getCapacity() -1, "a");
        subject.removeLast();

        assertThat(subject.isEmpty(), is(false));
        assertThat(subject.size(), is(subject.getCapacity() - 1));
    }

    @Test
    public void size() {
        assertThat(subject.size(), is(0));
    }

    @Test
    public void add() {
        final String value = "2";

        subject.add(value);

        assertThat(subject.size(), is(1));
    }

    @Test
    public void add_multiple() {
        final int size = 100;
        final List<String> list = getRandomValues(size);

        for (String s : list) {
            subject.add(s);
        }

        assertThat(subject.size(), is(size));
        for (int i = 0; i < subject.size(); i++) {
            assertThat(subject.get(i), is(list.get(i)));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void set_before() {
        subject.set(-1, "1");
    }

    @Test
    public void set_afterGrows() {
        final int capacity = subject.getCapacity();

        subject.set(capacity, "1");

        assertThat(subject.getCapacity(), is(greaterThan(capacity)));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void remove_index() {
        subject.remove(Bag.DEFAULT_CAPACITY);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void remove_negativeIndex() {
        subject.remove(-1);
    }

    @Test
    public void removeLast_empty() {
        assertThat(subject.removeLast(), is(nullValue()));
    }

    @Test
    public void removeLast() {
        final String secondLast = "secondLast";
        final String last = "last";

        subject.add(last);
        subject.add(secondLast);

        assertThat(subject.removeLast(), is(secondLast));
        assertThat(subject.removeLast(), is(last));
        assertThat(subject.isEmpty(), is(true));
    }

    @Test
    public void remove_object() {
        final List<String> list = getRandomValues(100);
        populateWithRandomValues(subject, list);
        Collections.shuffle(list);

        for (String s : list) {
            subject.remove(s);
        }

        assertThat(subject.isEmpty(), is(true));
    }

    @Test
    public void remove_missingObject() {
        assertThat(subject.remove("sonotinhere!"), is(false));
    }

    @Test
    public void contains() {
        final String s = "so in here";
        subject.add(s);

        assertThat(subject.contains(s), is(true));
    }

    @Test
    public void contains_not() {
        subject.add("so in here");

        assertThat(subject.contains("so not in here!"), is(false));
    }

    @Test
    public void contains_doesNotEquals() {
        final String s = "so in here";
        subject.add(s);

        assertThat(subject.contains(new String(s)), is(false));
    }

    @Test
    public void clear() {
        populateWithRandomValues(subject, getRandomValues(100));

        subject.clear();

        assertThat(subject.size(), is(0));
        assertThat(subject.isEmpty(), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeAll_self() {
        subject.removeAll(subject);
    }

    @Test
    public void removeAll_all() {
        final List<String> randomValues = getRandomValues(100);
        populateWithRandomValues(subject, randomValues);

        Collections.shuffle(randomValues);
        Bag<String> bag =  populateWithRandomValues(new Bag<String>(), randomValues);

        final boolean modified = subject.removeAll(bag);

        assertThat(modified, is(true));
        assertThat(subject.size(), is(0));
        assertThat(subject.isEmpty(), is(true));
    }

    @Test
    public void removeAll_smallerSource() {
        subject.add("1");
        final Bag<String> bag = new Bag<String>();
        bag.add("1");
        bag.add("2");

        final boolean modified = subject.removeAll(bag);

        assertThat(modified, is(true));
        assertThat(subject.size(), is(0));
        assertThat(subject.isEmpty(), is(true));
    }

    @Test
    public void removeAll_doesNotEquals() {
        subject.add("1");
        final Bag<String> bag = new Bag<String>();
        bag.add(new String("1"));

        final boolean modified = subject.removeAll(bag);

        assertThat(modified, is(false));
        assertThat(subject.isEmpty(), is(false));
    }

    @Test
    public void addAll() {
        final int size = 10;
        populateWithRandomValues(subject, getRandomValues(size));
        final Bag<String> bag = populateWithRandomValues(new Bag<String>(), getRandomValues(size));

        subject.addAll(bag);

        assertThat(subject.size(), is(size + size));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_self() {
        subject.addAll(subject);
    }

    private List<String> getRandomValues(int size) {
        final Random random = new Random();

        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            list.add(Integer.toString(random.nextInt(size)));
        }

        return list;
    }

    private Bag<String> populateWithRandomValues(Bag<String> bag, List<String> list) {
        for (String s : list) {
            bag.add(s);
        }

        return bag;
    }
}