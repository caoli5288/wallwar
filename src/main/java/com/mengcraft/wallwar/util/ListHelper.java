package com.mengcraft.wallwar.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created on 16-3-9.
 */
public final class ListHelper {

    public static <T> void forEach(Collection<T> i, Predicate<T> p, Consumer<T> c) {
        i.forEach(t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        });
    }

    public static <T> void forEachRemaining(Iterator<T> i, Predicate<T> p, Consumer<T> c) {
        i.forEachRemaining(t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        });
    }

    public static <T> boolean any(Collection<T> input, Predicate<T> p) {
        for (T i : input) {
            if (p.test(i)) return true;
        }
        return false;
    }

}
