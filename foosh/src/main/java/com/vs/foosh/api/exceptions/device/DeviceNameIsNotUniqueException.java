package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import com.vs.foosh.api.model.device.DeviceNamePatchRequest;

public class DeviceNameIsNotUniqueException extends RuntimeException {
    private DeviceNamePatchRequest request;

    public DeviceNameIsNotUniqueException(DeviceNamePatchRequest request) {
        super("The name '" + request.name() + "' is already used!");
        this.request = request;
    }

    public UUID getId() {
        return this.request.id();
    }
}
