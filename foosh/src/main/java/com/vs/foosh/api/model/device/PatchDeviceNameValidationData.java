package com.vs.foosh.api.model.device;

import java.util.UUID;

public record PatchDeviceNameValidationData(String identifier, UUID uuid, String name) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< PatchDeviceNameValidationData >>\n");
        builder.append("Identifier: " + identifier + "\n");
        builder.append("UUID: " + uuid + "\n");
        builder.append("Name: " + name);

        return builder.toString();
    }
}