package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.google.gson.JsonElement;

import java.util.HashSet;

public class BasicMetadataContainer<T> extends MetadataContainer<T> {

    private final HashSet<T> data = new HashSet<>();

    public BasicMetadataContainer(Class<T> type) {
        super(type);
    }

    public void addValue(T value) {
        if(data.contains(value)) return;
        this.data.add(value);
    }

    public JsonElement toJSON() {
        return GSON.toJsonTree(data);
    }

}
