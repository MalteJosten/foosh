package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

public class VariableDevicesPostRequest {
    private List<UUID> devices;

    public VariableDevicesPostRequest() {}

    public VariableDevicesPostRequest(List<UUID> devices) {
        this.devices = devices;
    }

    public List<UUID> getDevices() {
        return this.devices;
    }
    
}
