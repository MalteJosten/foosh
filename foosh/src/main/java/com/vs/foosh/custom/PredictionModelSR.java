package com.vs.foosh.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.web.SmartHomeInstruction;

public class PredictionModelSR extends AbstractPredictionModel {

    public PredictionModelSR() {
        this.id   = UUID.randomUUID();
        this.name = "Symbolic Regression Model";

        ParameterMapping mapping = new ParameterMapping("x1", UUID.fromString("3bd47c41-a21b-4769-a177-33088646bf48"));
        setMapping(List.of(mapping));

        updateLinks();
    }

    @Override
    public List<SmartHomeInstruction> makePrediction(String value) {
        return new ArrayList<>();
    }
    
}
