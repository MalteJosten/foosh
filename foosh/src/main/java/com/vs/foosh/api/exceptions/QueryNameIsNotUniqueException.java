package com.vs.foosh.api.exceptions;

import java.util.UUID;

import com.vs.foosh.api.model.QueryNamePatchRequest;

public class QueryNameIsNotUniqueException extends RuntimeException {
    private QueryNamePatchRequest request;

    public QueryNameIsNotUniqueException(QueryNamePatchRequest request) {
        super("The query name '" + request.getQueryName() + "' is not unique!");
        this.request = request;
    }

    public UUID getId() {
        return this.request.getId();
    }
}
