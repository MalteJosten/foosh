package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

/**
 * A {@link Record} containing a request to set a {@link Variable}'s device IDs.
 * 
 * @param deviceIds the {@link List} of {@link AbstractDevice} IDs of type {@link UUID}
 */
public record VariableDevicesPostRequest(List<UUID> deviceIds) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableDevicesPostRequest >>\n");
        builder.append("Device-IDs: " + deviceIds);

        return builder.toString();
    }
    
}
