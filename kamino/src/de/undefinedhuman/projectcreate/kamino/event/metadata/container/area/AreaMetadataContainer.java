package de.undefinedhuman.projectcreate.kamino.event.metadata.container.area;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonElement;
import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataContainer;

public class AreaMetadataContainer extends MetadataContainer<Vector2> {

    private final Area area = new Area();

    public AreaMetadataContainer() {
        super(Vector2.class);
    }

    public void addValue(Vector2 value) {
        this.area.addPosition(value);
    }

    public JsonElement toJSON() {
        return GSON.toJsonTree(area);
    }

}
