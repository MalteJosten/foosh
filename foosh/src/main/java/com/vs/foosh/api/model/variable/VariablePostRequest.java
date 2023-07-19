package com.vs.foosh.api.model.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VariablePostRequest {
    private String name;
    private List<UUID> devices = new ArrayList<>();
    private List<UUID> models  = new ArrayList<>();
    
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<< VariablePostRequest >>");
        builder.append("name: \t" + name + "\n");
        builder.append("devices:\n");
        for(UUID id: devices) {
            builder.append("\t" + id + "\n");
        }
        builder.append("models:\n");
        for(UUID id: models) {
            builder.append("\t" + id + "\n");
        }

        return builder.toString();
    }
}
