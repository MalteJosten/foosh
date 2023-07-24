package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import com.vs.foosh.api.model.device.DeviceNamePatchRequest;

public class DeviceNameIsEmptyException extends RuntimeException {
    private DeviceNamePatchRequest request;

    public DeviceNameIsEmptyException(DeviceNamePatchRequest request) {
        super("The provided value for the field 'name' (" + request.getName() + ") is empty!");
        this.request = request;
    }

    public UUID getId() {
        return this.request.getId();
    }
}
