package com.mengcraft.wallwar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created on 15-9-26.
 */
public class SetPicker<T> implements Iterator<T> {

    private final List<T> set;
    private final Random random;

    public static <T> SetPicker<T> of(Collection<T> collection, Random random) {
        return new SetPicker<>(collection, random);
    }

    public static <T> SetPicker<T> of(T[] array, Random random) {
        return new SetPicker<>(array, random);
    }

    public SetPicker(Collection<T> collection, Random random) {
        this.set = new ArrayList<>(collection);
        this.random = random;
    }

    public SetPicker(T[] array, Random random) {
        this.set = new LinkedList<>(Arrays.asList(array));
        this.random = random;
    }

    @Override
    public T next() {
        return set.remove(random.nextInt(set.size()));
    }

    @Override
    public boolean hasNext() {
        return set.size() != 0;
    }
}
