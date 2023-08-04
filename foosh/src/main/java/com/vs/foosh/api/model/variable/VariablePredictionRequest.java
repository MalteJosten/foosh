package com.vs.foosh.api.model.variable;

public class VariablePredictionRequest {
    private String modelId;
    private String value;
    private boolean execute;

    public VariablePredictionRequest(String modelId, String value, boolean execute) {
        this.modelId = modelId;
        this.value   = value;
        this.execute = execute;
    }

    public String getModelId() {
        return this.modelId;
    }

    public String getValue() {
        return this.value;
    }

    public boolean getExecute() {
        return this.execute;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariablePredictionRequest >>\n");
        builder.append("Model: " + modelId + "\n");
        builder.append("Value: " + value   + "\n");
        builder.append("Execute Instructions: " + execute);

        return builder.toString();
    }

}
