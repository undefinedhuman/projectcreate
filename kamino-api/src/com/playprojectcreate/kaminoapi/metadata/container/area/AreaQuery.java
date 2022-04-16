package com.playprojectcreate.kaminoapi.metadata.container.area;

import com.badlogic.gdx.math.Vector2;
import com.playprojectcreate.kaminoapi.query.Query;
import com.playprojectcreate.kaminoapi.query.QueryParameterWrapper;

public class AreaQuery extends Query<Area, Float> {

    public AreaQuery() {
        super(Area.class);
    }

    @Override
    public QueryParameterWrapper<Float>[] parseQueryParametersFromRequest(String metadataKey, Area area) {
        return new QueryParameterWrapper[] {
                new QueryParameterWrapper<>("minX", area.getMinX()),
                new QueryParameterWrapper<>("minY", area.getMinY()),
                new QueryParameterWrapper<>("maxX", area.getMaxX()),
                new QueryParameterWrapper<>("maxY", area.getMaxY())
        };
    }

    @Override
    public String createQuery(String fieldName, String[] parameterNames) {
        return "NOT (" +
                fieldName + ".min.x" +
                " > " +
                "$" + parameterNames[2] +
                " OR " +
                fieldName + ".max.x" +
                " < " +
                "$" + parameterNames[0] +
                " OR " +
                fieldName + ".max.y" +
                " < " +
                "$" + parameterNames[1] +
                " OR " +
                fieldName + ".min.y" +
                " > " +
                "$" + parameterNames[3] +
                ")";
    }

    @Override
    public <T> boolean doesEventMatch(Area area, T eventData) {
        if(!(eventData instanceof Vector2 vector)) return false;
        return vector.x >= area.getMinX() && vector.x <= area.getMaxX() && vector.y >= area.getMinY() && vector.y <= area.getMaxY();
    }

}
