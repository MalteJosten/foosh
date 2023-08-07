package com.vs.foosh.api.model.device;

import java.util.UUID;

public record DeviceNamePatchRequest(UUID id, String name){
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceNamePatchRequest >>\n");
        builder.append("ID:   " + id + "\n");
        builder.append("Name: " + name);
        
        return builder.toString();
    }
}