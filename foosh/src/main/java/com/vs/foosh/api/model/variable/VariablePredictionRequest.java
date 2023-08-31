package com.vs.foosh.api.model.variable;

import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.services.helpers.ListService;

/**
 * A {@link Record} containing a request to make a prediction for a {@link Variable}, given a {@link AbstractPredictionModel} and a {@code value}.
 * 
 * @param modelId the {@link UUID} of the {@link AbstractPredictionModel} that shall be used for making the prediction
 * @param value the prediction value
 * @param execute indicates whether the resulting {@link List} of {@link SmartHomeInstruction}s shall be executed
 */
public record VariablePredictionRequest(String modelId, String value, boolean execute) {

    /**
     * Validate the {@code VariablePredictionRequest}.
     */
    public void validate(String variableId) {
        ListService.getVariableList().getThing(variableId);

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

        if (value.trim().isEmpty()) {
            throw new MalformedVariablePredictionRequest(
                variableId,
                "Could not read prediciton request. The field 'value' is empty.");
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
