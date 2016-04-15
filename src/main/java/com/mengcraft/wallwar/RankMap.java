package com.mengcraft.wallwar;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16-4-14.
 */
public class RankMap<T> extends EnumMap<Rank, T> {

    public RankMap() {
        super(Rank.class);
    }

    public Map<String, T> toMap() {
        Map<String, T> map = new HashMap<>();
        forEach((k, v) -> {
            map.put(k.name(), v);
        });
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> RankMap<T> from(Map<String, Object> in) {
        RankMap<T> map = new RankMap<>();
        in.forEach((k, v) -> {
            map.put(Rank.valueOf(k.toUpperCase()), (T) v);
        });
        return map;
    }

}
