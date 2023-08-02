package com.vs.foosh.api.model.predictionModel;

import java.util.List;

import com.vs.foosh.api.model.misc.Thing;

// TODO: @Override toString()
public class PredictionModelResponseObject extends Thing {
    private List<String> parameters;
    private List<ParameterMapping> mapping;

    public PredictionModelResponseObject(AbstractPredictionModel model) {
        this.id         = model.getId();
        this.name       = model.getName();
        this.parameters = model.getParameters();
        this.mapping    = model.getMapping();
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public List<ParameterMapping> getMapping() {
        return this.mapping;
    }

}
