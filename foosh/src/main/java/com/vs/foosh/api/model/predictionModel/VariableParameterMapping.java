package com.vs.foosh.api.model.predictionModel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class VariableParameterMapping implements Serializable {
    private UUID variableId;
    private List<ParameterMapping> mappings;

    public VariableParameterMapping() {}

    public VariableParameterMapping(UUID variableId, List<ParameterMapping> mappings) {
        this.variableId = variableId;
        this.mappings   = mappings;
    }

    public void clearMappings() {
        this.mappings.clear();
    }

    public UUID getVariableId() {
        return this.variableId;
    }

    public List<ParameterMapping> getMappings() {
        return this.mappings;
    }
}
