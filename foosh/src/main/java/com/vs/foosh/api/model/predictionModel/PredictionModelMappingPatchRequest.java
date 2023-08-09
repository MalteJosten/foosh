package com.vs.foosh.api.model.predictionModel;

import java.util.Map;
import java.util.UUID;

public class PredictionModelMappingPatchRequest {
    private String parameter;
    private UUID deviceId;


    public PredictionModelMappingPatchRequest(String modelId, Map<String, String> mapping) {
        this.parameter = mapping.get("parameter");
        this.deviceId  = UUID.fromString(mapping.get("deviceId"));
    }

    public String getParameter() {
        return this.parameter;
    }

    public UUID getDeviceId() {
        return this.deviceId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< PredictionModelMappingPatchRequest >>\n");
        builder.append("Parameter: " + parameter + "\n");
        builder.append("Device-ID: " + deviceId);

        return builder.toString();
    }
    
}
