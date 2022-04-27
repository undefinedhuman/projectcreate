package com.playprojectcreate.kaminoapi.metadata.container.area;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonElement;
import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;
import com.playprojectcreate.kaminoapi.query.QueryParameter;

@QueryParameter(type = Area.class)
public class AreaMetadataContainer extends MetadataContainer<Vector2> {

    private final Area area = new Area();
    private final AreaQuery query = new AreaQuery();

    public AreaMetadataContainer() {
        super(Vector2.class);
    }

    @Override
    public AreaQuery getQuery() {
        return query;
    }

    public void addValue(Vector2 value) {
        this.area.addPosition(value);
    }

    public JsonElement toJSON() {
        return GSON.toJsonTree(area);
    }

}
