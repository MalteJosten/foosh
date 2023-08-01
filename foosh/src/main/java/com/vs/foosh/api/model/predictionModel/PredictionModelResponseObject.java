package com.vs.foosh.api.model.predictionModel;

import java.util.List;

import com.vs.foosh.api.model.misc.Thing;

// TODO: @Override toString()
public class PredictionModelResponseObject extends Thing {
    private List<ParameterMapping> mapping;

    public PredictionModelResponseObject(AbstractPredictionModel model) {
        this.id      = model.getId();
        this.name    = model.getName();
        this.mapping = model.getMapping();
    }

    public List<ParameterMapping> getMapping() {
        return this.mapping;
    }

}
