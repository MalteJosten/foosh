package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingDeviceException;
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
        IdService.isUuidInList(variableId, ListService.getVariableList().getList());

        // Detect and remove duplicates
        HashMap<String, Integer> parameterCounter = new HashMap<>();
        for (ParameterMapping mapping: mappings) {
            if (!parameterCounter.keySet().contains(mapping.getParameter())) {
                parameterCounter.put(mapping.getParameter(), 0);
            }
            parameterCounter.put(mapping.getParameter(), parameterCounter.get(mapping.getParameter()) + 1);
        }

        for (Entry<String, Integer> parameterCount: parameterCounter.entrySet()) {
            if (parameterCount.getValue() > 1) {
                throw new MalformedParameterMappingException(modelId, "You assigned parameter '" + parameterCount.getKey() + "' multiple times. You can only assign each parameter once!");
            }
        }


        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(modelId.toString());

        // Validate entries.
        for (ParameterMapping mapping: mappings) {

            // Parameter validation
            String parameter = mapping.getParameter();
            if (parameter == null) {
                throw new MalformedParameterMappingException(modelId, "A field called 'parameter' is missing!");
            } else if (parameter.trim().isEmpty()) {
                throw new MalformedParameterMappingException(modelId, "The value of the field 'parameter' must not be empty!");
            }

            if (!model.getParameters().contains(parameter)) {
                throw new MalformedParameterMappingException(modelId, "The model does not have a parameter called '" + parameter + "'!");
            }

            // Device-ID validation
            String deviceId = mapping.getDeviceId();
            if (deviceId == null) {
                throw new MalformedParameterMappingException(modelId, "A field called 'deviceId' is missing!");
            } else if (deviceId.trim().isEmpty()) {
                throw new MalformedParameterMappingException(modelId, "The value of the field 'deviceId' must not be empty!");
            }

            UUID deviceUuid = IdService.isUuid(deviceId).orElseThrow(() -> new MalformedParameterMappingException(modelId, "The given deviceId '" + deviceId + "' is not a valid UUID!"));

            if (!ListService.getVariableList().getThing(variableId.toString()).getDeviceIds().contains(deviceUuid)) {
                throw new ParameterMappingDeviceException(modelId, variableId, deviceUuid);
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
