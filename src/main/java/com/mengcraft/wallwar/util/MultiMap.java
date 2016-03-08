package com.mengcraft.wallwar.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 15-12-23.
 */
public class MultiMap<K, V> {

    private final Map<K, List<V>> map;
    private final ListFactory<V> listFactory;

    public MultiMap(Map<K, List<V>> map, ListFactory<V> listFactory) {
        this.map = map;
        this.listFactory = listFactory;
    }

    public MultiMap(Map<K, List<V>> map) {
        this(map, () -> new ArrayList<>());
    }

    public MultiMap() {
        this(new HashMap<>(), () -> new ArrayList<>());
    }

    public void clear() {
        map.clear();
    }

    public List<V> remove(K key) {
        return map.remove(key);
    }

    public boolean remove(K key, V value) {
        List<V> list = map.get(key);
        if (list != null) {
            return list.remove(value);
        }
        return false;
    }

    public boolean has(K key) {
        return map.get(key) != null;
    }

    /**
     * @param key The key.
     * @return The copy of value list.
     */
    public List<V> get(K key) {
        List<V> list = map.get(key);
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    public V getFirst(K key) {
        List<V> list = get(key);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public V getLast(K key) {
        List<V> list = get(key);
        if (list != null && list.size() != 0) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    public int getSize() {
        int size = 0;
        for (List<V> list : map.values()) {
            size += list.size();
        }
        return size;
    }

    public int getDeep() {
        return map.size();
    }

    public boolean put(K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            map.put(key, list = listFactory.createList());
        }
        return list.add(value);
    }

    public Map<K, List<V>> getMap() {
        return map;
    }

}
