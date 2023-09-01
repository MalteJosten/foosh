package com.vs.foosh.api.model.device;

import java.util.UUID;

/**
 * A {@link Record} holding information about a request to change the name of an {@link AbstractDevice}.
 * 
 * @param uuid the {@link UUID} of the {@link AbstractDevice} in question
 * @param name the requested {@link String} value to change the {@code name} to
 */
public record DeviceNamePatchRequest(UUID uuid, String name){
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceNamePatchRequest >>\n");
        builder.append("ID:   " + uuid + "\n");
        builder.append("Name: " + name);
        
        return builder.toString();
    }
}