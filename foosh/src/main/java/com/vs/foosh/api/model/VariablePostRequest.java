package com.vs.foosh.api.model;

import java.util.List;
import java.util.UUID;

public class VariablePostRequest {
    private String name;
    private List<UUID> devices;
    private List<UUID> models;
    
    public VariablePostRequest(String name, List<UUID> devices, List<UUID> models) {
        this.name    = name;
        this.devices = devices;
        this.models  = models;
    }

    public String getName() {
        return this.name;
    }

    public List<UUID> getDevices() {
        return this.devices;
    }

    public List<UUID> getModels() {
        return this.models;
    }
}
