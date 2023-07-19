package com.vs.foosh.api.model.device;

import java.util.UUID;

public class QueryNamePatchRequest {
    private UUID id;
    private String queryName;

    public QueryNamePatchRequest(UUID id, String queryName) {
        this.id = id;
        this.queryName = queryName.toLowerCase();
    }

    public UUID getId() {
        return this.id;
    }

    public String getQueryName() {
        return this.queryName;
    }
}
