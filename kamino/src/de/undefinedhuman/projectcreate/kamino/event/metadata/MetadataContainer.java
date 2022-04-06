package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;

public abstract class MetadataContainer<T> {

    protected static final Gson GSON = new GsonBuilder().create();

    private final Class<T> type;

    public MetadataContainer(Class<T> type) {
        this.type = type;
    }

    public boolean verifyField(Field field) {
        return MetadataUtils.wrapPrimitive(field.getType()) == type;
    }

    public abstract void addValue(T t);
    public abstract JsonElement toJSON();
    public Class<T> getType() {
        return type;
    }

}
