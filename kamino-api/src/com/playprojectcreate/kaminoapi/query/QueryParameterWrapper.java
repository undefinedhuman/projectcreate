package com.playprojectcreate.kaminoapi.query;

public record QueryParameterWrapper<QueryParameterType>(String key, QueryParameterType value) {}
