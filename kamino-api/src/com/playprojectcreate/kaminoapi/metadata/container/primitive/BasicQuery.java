package com.playprojectcreate.kaminoapi.metadata.container.primitive;

import com.playprojectcreate.kaminoapi.query.Query;
import com.playprojectcreate.kaminoapi.query.QueryParameterWrapper;

public class BasicQuery<T> extends Query<T, T> {

    public BasicQuery(Class<T> parameterType) {
        super(parameterType);
    }

    @Override
    public QueryParameterWrapper<T>[] parseQueryParametersFromRequest(String metadataKey, T t) {
        return new QueryParameterWrapper[] {
                new QueryParameterWrapper<>(metadataKey, t)
        };
    }

    @Override
    public String createQuery(String fieldName, String[] parameterNames) {
        return "ARRAY_CONTAINS(" + fieldName + ", $" + parameterNames[0] + ")";
    }

    @Override
    public <T1> boolean doesEventMatch(T t, T1 eventData) {
        return t.equals(eventData);
    }

}
