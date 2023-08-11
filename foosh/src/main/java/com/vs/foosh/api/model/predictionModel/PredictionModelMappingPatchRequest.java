package com.vs.foosh.api.model.predictionModel;

import java.util.Map;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;

public class PredictionModelMappingPatchRequest {
    private String parameter;
    private UUID deviceId;


    public PredictionModelMappingPatchRequest(String modelId, Map<String, String> mapping) {
        String pParameter = mapping.get("parameter");
        if (pParameter == null) {
            throw new MalformedParameterMappingException(
                modelId, 
                "A field called 'parameter' is missing!");
        }
        this.parameter = pParameter;

        try {
            this.deviceId = UUID.fromString(mapping.get("deviceId"));
        } catch (NullPointerException e) {
            throw new MalformedParameterMappingException(
                modelId, 
                "A field called 'deviceId' is missing!");
        } catch (IllegalArgumentException e) {
            throw new MalformedParameterMappingException(
                modelId, 
                "The given deviceId '" + mapping.get("deviceId") + "' is not a valid UUID!");
        }
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
