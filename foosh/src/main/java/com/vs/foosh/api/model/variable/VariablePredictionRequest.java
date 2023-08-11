package com.vs.foosh.api.model.variable;

import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.services.ListService;

public record VariablePredictionRequest(String modelId, String value, boolean execute) {

    public void validate(String variableId) {
        if (modelId == null) {
            throw new MalformedVariablePredictionRequest(
                variableId,
                "Could not read prediciton request. It's missing a field 'modelId'.");
        }

        if (modelId.trim().isEmpty()) {
            throw new MalformedVariablePredictionRequest(
                variableId,
                "Could not read prediciton request. The field 'modelId' is empty.");
        }

        if (value == null) {
            throw new MalformedVariablePredictionRequest(
                variableId,
                "Could not read prediciton request. It's missing the field 'value'.");
        }

        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(modelId);
        model.checkValueInBounds(variableId, value);

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariablePredictionRequest >>\n");
        builder.append("Model: "    + modelId + "\n");
        builder.append("Value: "    + value   + "\n");
        builder.append("Execute Instructions: " + execute);

        return builder.toString();
    }

}
