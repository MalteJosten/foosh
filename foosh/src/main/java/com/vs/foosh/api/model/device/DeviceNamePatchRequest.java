package com.vs.foosh.api.model.device;

import java.util.UUID;

public class DeviceNamePatchRequest {
    private UUID id;
    private String name;

    public DeviceNamePatchRequest(UUID id, String name) {
        this.id   = id;
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceNamePatchRequest >>\n");
        builder.append("ID:   " + id + "\n");
        builder.append("Name: " + name);
        
        return builder.toString();
    }
}
