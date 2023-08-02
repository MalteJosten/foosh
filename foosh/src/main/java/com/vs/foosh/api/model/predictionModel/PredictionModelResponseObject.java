package com.vs.foosh.api.model.predictionModel;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;

// TODO: @Override toString()
public class PredictionModelResponseObject extends Thing {
    private List<UUID> variableIds;
    private List<String> parameters;
    private List<VariableParameterMapping> mappings;

    public PredictionModelResponseObject(AbstractPredictionModel model) {
        this.id          = model.getId();
        this.name        = model.getName();
        this.variableIds = model.getVariableIds();
        this.parameters  = model.getParameters();
        this.mappings    = model.getAllMappings();
    }

    public List<UUID> getVariableIds() {
        return this.variableIds;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public List<VariableParameterMapping> getMappings() {
        return this.mappings;
    }

}
