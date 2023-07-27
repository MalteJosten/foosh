package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;

public class VariableResponseObject extends Thing {
    private List<UUID> models;
    private List<UUID> devices;
    

    public VariableResponseObject(Variable variable) {
        this.id      = variable.getId();
        this.name    = variable.getName();
        this.models  = variable.getModelIds();
        this.devices = variable.getDeviceIds();
    }

    public List<UUID> getModels() {
        return this.models;
    }

    public List<UUID> getDevices() {
        return this.devices;
    }

}
