package com.vs.foosh.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.web.SmartHomeInstruction;

public class PredictionModelSR extends AbstractPredictionModel {

    public PredictionModelSR() {
        this.id   = UUID.randomUUID();
        this.name = "Symbolic Regression Model";

        updateSelfLinks();
    }

    @Override
    public List<SmartHomeInstruction> makePrediction(String value) {
        return new ArrayList<>();
    }
    
}
