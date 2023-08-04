package com.vs.foosh.api.model.variable;

import java.util.UUID;

public class VariablePredictionRequest {
    private UUID modelId;
    private String value;

    public VariablePredictionRequest(UUID modelId, String value) {
        this.modelId = modelId;
        this.value   = value;
    }

    public UUID getModelId() {
        return this.modelId;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariablePredictionRequest >>\n");
        builder.append("Model: " + modelId + "\n");
        builder.append("Value: " + value);

        return builder.toString();
    }

}
