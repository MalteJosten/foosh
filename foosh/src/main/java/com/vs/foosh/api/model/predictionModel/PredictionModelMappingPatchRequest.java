package com.vs.foosh.api.model.predictionModel;

import java.util.Map;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;

/**
 * A class containing a request to change a {@link ParameterMapping}.
 */
public class PredictionModelMappingPatchRequest {
    /**
     * The name of the parameter.
     */
    private String parameter;
    
    /**
     * The {@link UUID} of the {@link AbstractDevice}.
     */
    private UUID deviceId;


    /**
     * Create a {@code PredictionModelMappingPatchRequest}.
     * 
     * If the request is malformed, a {@link MalformedParameterMappingException} is thrown.
     * 
     * @param modelId the identifier of the {@link AbstractPredicitonModel}
     * @param mapping a {@link Map} containing the mapping from {@code parameter} to {@code deviceId}
     */
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

    /**
     * Return the parameter name.
     * 
     * @return the field {@code parameter}
     */
    public String getParameter() {
        return this.parameter;
    }

    /**
     * Return the device ID.
     * 
     * @return the field {@code deviceId}
     */
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
