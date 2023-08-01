package com.vs.foosh.api.model.predictionModel;

import com.vs.foosh.api.model.misc.Thing;

// TODO: @Override toString()
public class PredictionModelResponseObject extends Thing {

    public PredictionModelResponseObject(AbstractPredictionModel model) {
        this.id   = model.getId();
        this.name = model.getName();
    }

}
