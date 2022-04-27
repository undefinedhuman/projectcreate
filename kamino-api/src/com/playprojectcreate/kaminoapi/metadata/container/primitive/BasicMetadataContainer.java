package com.playprojectcreate.kaminoapi.metadata.container.primitive;

import com.google.gson.JsonElement;
import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;

import java.util.HashSet;

public class BasicMetadataContainer<T> extends MetadataContainer<T> {

    private final HashSet<T> data = new HashSet<>();
    private final BasicQuery<T> query = new BasicQuery<>(getType());

    public BasicMetadataContainer(Class<T> type) {
        super(type);
    }

    @Override
    public BasicQuery<T> getQuery() {
        return query;
    }

    public void addValue(T value) {
        if (data.contains(value)) return;
        this.data.add(value);
    }

    public JsonElement toJSON() {
        return GSON.toJsonTree(data);
    }

}
