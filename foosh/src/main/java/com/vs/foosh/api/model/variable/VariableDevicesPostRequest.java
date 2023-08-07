package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

public class VariableDevicesPostRequest {
    private List<UUID> deviceIds;

    public VariableDevicesPostRequest() {}

    public VariableDevicesPostRequest(List<UUID> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public List<UUID> getDeviceIds() {
        return this.deviceIds;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableDevicesPostRequest >>\n");
        builder.append("Device-IDs: " + deviceIds);

        return builder.toString();
    }
    
}
