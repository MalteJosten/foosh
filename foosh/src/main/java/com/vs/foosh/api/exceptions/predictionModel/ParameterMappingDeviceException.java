package com.vs.foosh.api.exceptions.predictionModel;

import java.util.UUID;

import com.vs.foosh.api.services.ListService;

public class ParameterMappingDeviceException extends RuntimeException {
    private String modelId;
    private UUID variableId;
    private UUID deviceId;
    
    public ParameterMappingDeviceException(String modelId, UUID variableId, UUID deviceId) {
        super("The device '" + ListService.getDeviceList().getThing(deviceId.toString()).getDeviceName() + "' (" + deviceId +
                ") is not assigned to the variable '" + ListService.getVariableList().getThing(variableId.toString()).getName() + "' (" + variableId + ")!");
        this.modelId    = modelId;
        this.variableId = variableId;
        this.deviceId   = deviceId;
    }

    public String getModelId() {
        return this.modelId;
    }

    public UUID getVariableId() {
        return this.variableId;
    }

    public UUID getDeviceId() {
        return this.deviceId;
    }
}
