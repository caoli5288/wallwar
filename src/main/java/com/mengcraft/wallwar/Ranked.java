package com.mengcraft.wallwar;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16-4-14.
 */
public class Ranked<T> extends EnumMap<Rank, T> {

    public Ranked() {
        super(Rank.class);
    }

    public Map<String, T> map() {
        Map<String, T> map = new HashMap<>();
        forEach((k, v) -> {
            map.put(k.name(), v);
        });
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> Ranked<T> of(Map<String, Object> in) {
        Ranked<T> map = new Ranked<>();
        in.forEach((k, v) -> {
            map.put(Rank.valueOf(k.toUpperCase()), (T) v);
        });
        return map;
    }

}
