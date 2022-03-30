package de.undefinedhuman.projectcreate.engine.utils.ds;

import java.util.*;
import java.util.stream.Collectors;

public class MultiMap<K, V> {

    private final HashMap<K, ArrayList<V>> data = new HashMap<>();
    private final HashMap<K, List<V>> immutableData = new HashMap<>();

    @SafeVarargs
    public final ArrayList<V> add(K key, V... values) {
        ArrayList<V> currentList = data.computeIfAbsent(key, k -> new ArrayList<>());
        immutableData.computeIfAbsent(key, k -> Collections.unmodifiableList(currentList));
        Collections.addAll(currentList, values);
        return currentList;
    }

    public List<V> getDataForKey(K key) {
        if(!hasKey(key)) add(key);
        return data.get(key);
    }

    public void removeKey(K key) {
        if(!hasKey(key)) return;
        data.get(key).clear();
        immutableData.remove(key);
        data.remove(key);
    }

    public void removeValue(K key, V value) {
        if(!hasKey(key)) return;
        data.get(key).remove(value);
    }

    public boolean hasValue(K key, V value) {
        if(!hasKey(key)) return false;
        return data.get(key).contains(value);
    }

    public boolean hasKey(K key) {
        return data.containsKey(key);
    }

    public Set<K> keySet() {
        return data.keySet();
    }

    public void clearKey(K key) {
        if(!hasKey(key)) return;
        data.get(key).clear();
    }

    public void clear() {
        for(ArrayList<V> list : data.values())
            list.clear();
        immutableData.clear();
        data.clear();
    }

    @Override
    public String toString() {
        return super.toString() + data.keySet().stream().map(key -> key.toString() + ": " + data.get(key).toString()).collect(Collectors.joining("\n"));
    }
}
