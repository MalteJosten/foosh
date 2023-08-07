package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

public class DeviceNameIsEmptyException extends RuntimeException {
    private UUID id;

    public DeviceNameIsEmptyException(UUID id, String name) {
        super("The provided value for the field 'name' (" + id + ") is empty!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
