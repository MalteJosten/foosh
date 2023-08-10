package com.vs.foosh.api.model.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPatchRequest;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueException;
import com.vs.foosh.api.services.IdService;

public class FooSHJsonPatch {
    private Map<String, Object> request;
    private FooSHPatchOperation operation;
    private String path;
    private Object value;
    private String parentId;

    public Map<String, Object> getRequest() {
        return this.request;
    }

    public FooSHJsonPatch() {}

    public FooSHJsonPatch(Map<String, Object> request) {
        this.request = request;
    }

    public void setParentId(String id) {
        this.parentId = id;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void validateRequest(List<FooSHPatchOperation> allowedOperations) {
        String operationField = (String) request.get("op");

        // Does the Patch contain a valid operation?
        try {
            operation = FooSHPatchOperation.valueOf(operationField.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FooSHJsonPatchIllegalArgumentException(parentId, "The operation '" + operationField + "' is not a valid Json Patch operation. Please visit https://www.rfc-editor.org/rfc/rfc6902#section-4 for a list of valid operations.");
        }

        // Do we allow this operation?
        if (!allowedOperations.contains(operation)) {
            throw new FooSHJsonPatchIllegalOperationException(operation);
        }

    }

    @SuppressWarnings("rawtypes")
    public void validateAdd(Class valueClass) {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() != 3) {
            throw new FooSHJsonPatchFormatException(parentId); 
        }

        this.path = (String) request.get("path");

        if (path == null || path.trim().isEmpty()) {
            throw new FooSHJsonPatchFormatException(parentId);
        }
        
        Object value = request.get("value");

        if (valueClass == String.class) {
            String stringValue = (String) value;
            if (stringValue.trim().isEmpty()) {
                throw new FooSHJsonPatchValueIsEmptyException(parentId);
            }

            validateValueAsString(stringValue);
        } else if (valueClass == UUID.class) {
            String uuidValue = (String) value;
            validateValueAsUUID(uuidValue);
        } else if (valueClass == PredictionModelMappingPatchRequest.class) {
            validateValueAsListOfPredictionModelMappingPatchRequests(value);
        } else {
            throw new FooSHJsonPatchValueException(parentId);
        }
    }

    @SuppressWarnings("rawtypes")
    public void validateReplace(Class valueClass) {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() != 3) {
            throw new FooSHJsonPatchFormatException(parentId); 
        }

        this.path = (String) request.get("path");
        
        if (path == null || path.trim().isEmpty()) {
            throw new FooSHJsonPatchFormatException(parentId);
        }

        Object value = request.get("value");

        if (valueClass == String.class) {
            String stringValue = (String) value;
            if (stringValue.trim().isEmpty()) {
                throw new FooSHJsonPatchValueIsEmptyException(parentId);
            }

            validateValueAsString(stringValue);
        } else if (valueClass == UUID.class) {
            String uuidValue = (String) value;
            validateValueAsUUID(uuidValue);
        } else if (valueClass == PredictionModelMappingPatchRequest.class) {
            validateValueAsListOfPredictionModelMappingPatchRequests(value);
        } else {
            throw new FooSHJsonPatchValueException(parentId);
        }
    }

    @SuppressWarnings("rawtypes")
    public void validateRemove(Class valueClass) {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() > 3) {
            if (!keys.containsAll(List.of("op", "path"))) {
                throw new FooSHJsonPatchFormatException(parentId); 
            }
        }

        this.path = (String) request.get("path");

        if (path == null || path.trim().isEmpty()) {
            throw new FooSHJsonPatchFormatException(parentId);
        }

        this.value = null;
    }

    private void validateValueAsString(String value) {
        if (IdService.isUuid(value).isPresent()) {
            throw new FooSHJsonPatchValueException(parentId);
        }

        this.value = value;
    }

    private void validateValueAsUUID(String value) {
        if (IdService.isUuid(value).isEmpty()) {
            throw new FooSHJsonPatchValueException(parentId);
        }

        this.value = value;
    }

    @SuppressWarnings("unchecked")
    private void validateValueAsListOfPredictionModelMappingPatchRequests(Object value) {
        try {
            ArrayList<Object> valueItems = (ArrayList<Object>) value;

            List<PredictionModelMappingPatchRequest> patches = new ArrayList<>();
            for (Object valueItem: valueItems) {
                PredictionModelMappingPatchRequest patchRequest = new PredictionModelMappingPatchRequest(parentId, (HashMap<String, String>) valueItem);
                patches.add(patchRequest);
            }

            this.value = patches;
        } catch (ClassCastException e) {
            throw new FooSHJsonPatchValueException(parentId);
        }

    }

    public boolean isValidPath(List<String> validPaths) {
        boolean isValid = false;

        if (path.trim().isEmpty()) {
            return isValid;
        }

        for (String validPath: validPaths) {
            if (validPath.equalsIgnoreCase("uuid")) {
                IdService.isUuid(path.split("/")[1]).orElseThrow(() -> new IdIsNoValidUUIDException(path.split("/")[1]));
                isValid = true;
                break;
            }

            if (path.equalsIgnoreCase(validPath)) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    public FooSHPatchOperation getOperation() {
        return this.operation;
    }

    public String getPath() {
        return this.path;
    }

    public String getDestination() {
        return this.path.split("/")[this.path.split("/").length - 1];
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< FooSHJsonPatch >>\n");
        builder.append("Operation: " + operation + "\n");
        builder.append("Path:      " + path + "\n");
        builder.append("Value:     " + value);

        return builder.toString();
    }
}
