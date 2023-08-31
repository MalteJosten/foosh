package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.predictionModel.ParameterMapping;


/**
 * A {@link Record} containing a request to link a {@link AbstractPredictionModel} to a {@link Variable}.
 * 
 * @param modelId the ID of type {@link UUID} of an {@link AbstractPredictionModel}
 * @param mappings the {@link List} of {@link ParameterMapping}s
 */
public record VariableModelPostRequest(UUID modelId, List<ParameterMapping> mappings) {
    
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
