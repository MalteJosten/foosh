package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.predictionModel.ParameterMapping;

public class VariableModelPostRequest {
    private UUID modelId;
    private List<ParameterMapping> mappings;

    public VariableModelPostRequest() {}

    public VariableModelPostRequest(UUID modelId, List<ParameterMapping> mappings) {
        this.modelId  = modelId;
        this.mappings = mappings;
    }

    public UUID getModelId() {
        return this.modelId;
    }

    public List<ParameterMapping> getMappings() {
        return this.mappings;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableModelPostRequest >>\n");
        builder.append("Model-Id: " + modelId + "\n");
        builder.append("Mappings:");

        for(ParameterMapping mapping: mappings) {
            builder.append("\n");
            builder.append(mapping.toString("\t"));
        }

        return builder.toString();
    }

    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder(prefix + "<< VariableModelPostRequest >>\n");
        builder.append("Model-Id: " + modelId + "\n");
        builder.append(prefix + "Mappings:");

        for(ParameterMapping mapping: mappings) {
            builder.append("\n");
            builder.append(prefix + mapping.toString(prefix + "\t"));
        }

        return builder.toString();
    }

}
