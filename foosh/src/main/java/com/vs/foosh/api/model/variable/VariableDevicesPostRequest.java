package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

public record VariableDevicesPostRequest(List<UUID> deviceIds) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableDevicesPostRequest >>\n");
        builder.append("Device-IDs: " + deviceIds);

        return builder.toString();
    }
    
}
