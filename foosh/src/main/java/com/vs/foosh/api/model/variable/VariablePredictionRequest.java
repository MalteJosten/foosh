package com.vs.foosh.api.model.variable;

public record VariablePredictionRequest(String modelId, String value, boolean execute) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariablePredictionRequest >>\n");
        builder.append("Model: " + modelId + "\n");
        builder.append("Value: " + value   + "\n");
        builder.append("Execute Instructions: " + execute);

        return builder.toString();
    }

}
