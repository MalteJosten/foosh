package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;
import com.vs.foosh.api.services.IdService;
import com.vs.foosh.api.services.ListService;

public class PredictionModelMappingPostRequest {
    private UUID variableId;
    private List<ParameterMapping> mappings = new ArrayList<>();

    public PredictionModelMappingPostRequest() {}

    public PredictionModelMappingPostRequest(UUID variableId, List<ParameterMapping> mappings) {
        this.variableId = variableId;
        this.mappings   = mappings;
    }

    public void validate(String modelId, List<UUID> validDeviceIds) {
        // Validate that the provided variableId is actually a valid variable id.
        IdService.isUuidInList(variableId, ListService.getVariableList().getList());

        // Detect and remove duplicates
        HashMap<String, Integer> parameterCounter = new HashMap<>();
        for (ParameterMapping mapping: mappings) {
            if (!parameterCounter.keySet().contains(mapping.getParameter())) {
                parameterCounter.put(mapping.getParameter(), 0);
            }
            parameterCounter.put(mapping.getParameter(), parameterCounter.get(mapping.getParameter()) + 1);
        }

        System.out.println(parameterCounter);

        for (Entry<String, Integer> parameterCount: parameterCounter.entrySet()) {
            if (parameterCount.getValue() > 1) {
                throw new MalformedParameterMappingException(modelId, "You assigned parameter '" + parameterCount.getKey() + "' multiple times. You can only assign each parameter once!");
            }
        }


        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(modelId);

        // Validate entries.
        for (ParameterMapping mapping: mappings) {

            // Is there a non-empty field called "parameter"?
            String parameter = mapping.getParameter();
            if (parameter == null || parameter.trim().isEmpty()) {
                throw new MalformedParameterMappingException(modelId, "The value of the field 'parameter' must not be empty!");
            }

            if (!model.getParameters().contains(parameter)) {
                throw new MalformedParameterMappingException(modelId, "The model does not have a parameter called '" + parameter + "'!");
            }

        }
    }

    public UUID getVariableId() {
        return this.variableId;
    }

    public List<ParameterMapping> getMappings() {
        return this.mappings;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< PredictionModelMappingPostRequest >>\n");
        builder.append("Variable-ID: " + variableId + "\n");
        builder.append("Mappings:");

        for(ParameterMapping mapping: mappings) {
            builder.append("\n");
            builder.append(mapping.toString("\t"));
        }

        return builder.toString();
    }

    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder(prefix + "<< PredictionModelMappingPostRequest >>\n");
        builder.append(prefix + "Variable-ID:\t" + variableId + "\n");
        builder.append(prefix + "Mappings:");

        for(ParameterMapping mapping: mappings) {
            builder.append("\n");
            builder.append(prefix + mapping.toString(prefix + "\t"));
        }

        return builder.toString();
    }
}
