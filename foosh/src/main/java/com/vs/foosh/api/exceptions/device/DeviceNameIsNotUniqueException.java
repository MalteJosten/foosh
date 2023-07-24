package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import com.vs.foosh.api.model.device.QueryNamePatchRequest;

public class DeviceNameIsNotUniqueException extends RuntimeException {
    private QueryNamePatchRequest request;

    public DeviceNameIsNotUniqueException(QueryNamePatchRequest request) {
        super("The query name '" + request.getQueryName() + "' is not unique!");
        this.request = request;
    }

    public UUID getId() {
        return this.request.getId();
    }
}
