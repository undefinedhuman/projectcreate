package com.playprojectcreate.kaminoapi.query;

public abstract class Query<ParameterType, ParameterInputType> {

    private final Class<ParameterType> parameterType;

    public Query(Class<ParameterType> parameterType) {
        this.parameterType = parameterType;
    }

    public abstract QueryParameterWrapper<ParameterInputType>[] parseQueryParametersFromRequest(String metadataKey, ParameterType parameterType);
    public abstract String createQuery(String fieldName, String[] parameterNames);
    public abstract <T> boolean doesEventMatch(ParameterType parameterType, T eventData);

    public Class<ParameterType> getType() {
        return parameterType;
    }

}
