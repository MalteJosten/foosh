package com.vs.foosh.api.model.predictionModel;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.web.SmartHomeInstruction;

public interface IPredictionMaker {
    
    /**
     * Make a prediction based on the provided {@code value}.
     * 
     * @param variableId the {@link UUID} of the variable for which we perform the prediction
     * @param value the value for which we make the prediction
     */
    public List<SmartHomeInstruction> makePrediction(UUID variableId, String value);
}
