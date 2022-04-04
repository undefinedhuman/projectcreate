package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;

public class AreaMetadataContainer {

    private static final Gson GSON = new Gson();

    private final Area area = new Area();

    public AreaMetadataContainer() {}

    public void addValue(Vector2 value) {
        this.area.addPosition(value);
    }

    public JsonElement toJSON() {
        return GSON.toJsonTree(area);
    }

    public boolean verifyField(Field field) {
        return field.getType() == Vector2.class;
    }

}
