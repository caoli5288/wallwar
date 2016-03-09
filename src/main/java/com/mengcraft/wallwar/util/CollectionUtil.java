package com.mengcraft.wallwar.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created on 16-3-9.
 */
public final class CollectionUtil {

    public static <T> void forEach(Collection<T> collection, Predicate<T> predicate, Consumer<T> consumer) {
        for (T element : collection) {
            if (predicate.test(element)) {
                consumer.accept(element);
            }
        }
    }

}
