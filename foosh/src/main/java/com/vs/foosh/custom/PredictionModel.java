package com.vs.foosh.custom;

import java.util.ArrayList;
import java.util.List;

import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.web.SmartHomeInstruction;

public class PredictionModel extends AbstractPredictionModel {

    @Override
    public List<SmartHomeInstruction> makePrediction(String value) {
        return new ArrayList<>();
    }
    
}
