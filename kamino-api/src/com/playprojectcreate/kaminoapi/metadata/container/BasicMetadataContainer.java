package com.playprojectcreate.kaminoapi.metadata.container;

import com.google.gson.JsonElement;
import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;

import java.util.HashSet;

public class BasicMetadataContainer<T> extends MetadataContainer<T> {

    private final HashSet<T> data = new HashSet<>();

    public BasicMetadataContainer(Class<T> type) {
        super(type);
    }

    public void addValue(T value) {
        if (data.contains(value)) return;
        this.data.add(value);
    }

    public String query(String tableName, String... parameterNames) {
        return "ANY id IN " + tableName + " SATISFIES ARRAY_CONTAINS($" + parameterNames[0] + ", id) END";
    }

    public JsonElement toJSON() {
        return GSON.toJsonTree(data);
    }

}
