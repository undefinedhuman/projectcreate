package de.undefinedhuman.projectcreate.engine.utils.ds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiMap<K, V> {

    private HashMap<K, ArrayList<V>> map = new HashMap<>();

    @SafeVarargs
    public final void add(K key, V... values) {
        if(map.containsKey(key)) map.get(key).addAll(Arrays.asList(values));
        else map.put(key, new ArrayList<>(Arrays.asList(values)));
    }

    public ArrayList<V> getValuesWithKey(K key) {
        if(!hasKey(key)) return new ArrayList<>();
        return map.get(key);
    }

    public void removeKey(K key) {
        if(!hasKey(key)) return;
        map.get(key).clear();
        map.remove(key);
    }

    public boolean removeValue(K key, V value) {
        if(!hasKey(key)) return false;
        return map.get(key).remove(value);
    }

    public boolean hasValue(K key, V value) {
        if(!hasKey(key)) return false;
        return map.get(key).contains(value);
    }

    public boolean hasKey(K key) {
        return map.containsKey(key);
    }

    public ArrayList<V> getAllValues() {
        ArrayList<V> values = new ArrayList<>();
        for(ArrayList<V> valueArray : map.values()) values.addAll(valueArray);
        return values;
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public void clear() {
        for(ArrayList<V> list : map.values()) list.clear();
        map.clear();
    }

    @Override
    public String toString() {
        return map.keySet().stream().map(key -> key.toString() + ": " + map.get(key).toString()).collect(Collectors.joining("\n"));
    }
}
