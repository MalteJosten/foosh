package com.vs.foosh.api.model.predictionModel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * A class containing an {@link Variable}'s {@link UUID} and its the parameter mappings.
 */
public class VariableParameterMapping implements Serializable {
    /**
     * The {@link UUID} of the {@link Variable}.
     */
    private UUID variableId;

    /**
     * The {@link List} with elements of type {@link ParameterMapping}.
     */
    private List<ParameterMapping> mappings;

    /**
     * Create a {@code VariableParameterMapping}.
     * 
     * @param variableId the {@link UUID} of a {@link Variable}
     * @param mappings the {@link List} with elements of type {@link ParameterMapping}
     */
    public VariableParameterMapping(UUID variableId, List<ParameterMapping> mappings) {
        this.variableId = variableId;
        this.mappings   = mappings;
    }

    /**
     * Delete the contents of {@code mappings}.
     */
    public void clearMappings() {
        this.mappings.clear();
    }

    /**
     * Return the variable ID.
     * 
     * @return the field {@code variableId}
     */
    public UUID getVariableId() {
        return this.variableId;
    }

    /**
     * Return the mappings.
     * 
     * @return the field {@code mappings}
     */
    public List<ParameterMapping> getMappings() {
        return this.mappings;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableParameterMapping >>\n");
        builder.append("Variable-ID: " + variableId + "\n");
        builder.append("Mappings: " + mappings);

        return builder.toString();
    }
}
