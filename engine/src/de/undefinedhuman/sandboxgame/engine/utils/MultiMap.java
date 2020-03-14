package de.undefinedhuman.sandboxgame.engine.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MultiMap<K, V> {

    private HashMap<K, ArrayList<V>> map;

    public MultiMap() {
        map = new HashMap<>();
    }

    public void add(K key, V... values) {
        if(map.containsKey(key)) map.get(key).addAll(Arrays.asList(values));
        else map.put(key, new ArrayList<>(Arrays.asList(values)));
    }

    public void remove(K key) {
        if(!hasKey(key)) return;
        map.remove(key);
    }

    private boolean hasKey(K key) {
        return map.containsKey(key);
    }

    public ArrayList<V> getAllValues() {
        ArrayList<V> values = new ArrayList<>();
        for(ArrayList<V> valueArray : map.values()) values.addAll(valueArray);
        return values;
    }

    public ArrayList<V> getValues(K key) {
        if(!hasKey(key)) return null;
        return map.get(key);
    }

    public HashMap<K, ArrayList<V>> getMap() {
        return map;
    }

    public void clear() {
        for(ArrayList<V> list : map.values()) list.clear();
        map.clear();
    }

}
