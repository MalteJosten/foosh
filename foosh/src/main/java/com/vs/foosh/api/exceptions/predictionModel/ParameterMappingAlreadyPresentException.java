package com.vs.foosh.api.exceptions.predictionModel;

import java.util.UUID;

public class ParameterMappingAlreadyPresentException extends RuntimeException {
    private UUID modelId;

    public ParameterMappingAlreadyPresentException(UUID modelId, String message) {
        super(message);
        this.modelId = modelId;
    }

    public UUID getModelId() {
        return this.modelId;
    }
    
}
