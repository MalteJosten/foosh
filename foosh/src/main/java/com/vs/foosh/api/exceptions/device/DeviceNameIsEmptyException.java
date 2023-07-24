package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import com.vs.foosh.api.model.device.QueryNamePatchRequest;

public class DeviceNameIsEmptyException extends RuntimeException {
    private QueryNamePatchRequest request;

    public DeviceNameIsEmptyException(QueryNamePatchRequest request) {
        super("The provided value for the field 'queryName' (" + request.getQueryName() + ") is empty!");
        this.request = request;
    }

    public UUID getId() {
        return this.request.getId();
    }
}
