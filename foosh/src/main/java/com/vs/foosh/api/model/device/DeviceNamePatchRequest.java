package com.vs.foosh.api.model.device;

import java.util.UUID;

// TODO: @Override toString()
public class DeviceNamePatchRequest {
    private UUID id;
    private String name;

    public DeviceNamePatchRequest(UUID id, String name) {
        this.id   = id;
        this.name = name.toLowerCase();
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
