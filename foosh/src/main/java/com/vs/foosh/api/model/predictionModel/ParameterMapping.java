package com.vs.foosh.api.model.predictionModel;

import java.util.UUID;

public class ParameterMapping {
    private String parameter;
    private UUID deviceId;

    public ParameterMapping(String parameter, UUID deviceId) {
        this.parameter = parameter;
        this.deviceId  = deviceId;
    }

    public String getParameter() {
        return this.parameter;
    }

    public UUID getDeviceId() {
        return this.deviceId;
    }

}
