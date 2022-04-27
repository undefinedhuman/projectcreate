package com.playprojectcreate.kaminoapi.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.playprojectcreate.kaminoapi.query.Query;

import java.lang.reflect.Field;

public abstract class MetadataContainer<T> {

    protected static final Gson GSON = new GsonBuilder().create();

    private final Class<T> type;

    public MetadataContainer(Class<T> type) {
        this.type = type;
    }

    public boolean verifyField(Field field) {
        return ContainerUtils.wrapPrimitive(field.getType()) == type;
    }

    public abstract <K, J> Query<K, J> getQuery();
    public abstract void addValue(T t);
    public abstract JsonElement toJSON();
    public Class<T> getType() {
        return type;
    }

}
