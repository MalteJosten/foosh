package com.vs.foosh.api.model.web;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueException;
import com.vs.foosh.api.services.IdService;

public class FooSHJsonPatch {
    private Map<String, String> request;
    private FooSHPatchOperation operation;
    private String path;
    private String value;
    private String parentId;

    public Map<String, String> getRequest() {
        return this.request;
    }

    public FooSHJsonPatch() {}

    public FooSHJsonPatch(Map<String, String> request) {
        this.request = request;
    }

    public void setParentId(String id) {
        this.parentId = id;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void validateRequest(List<FooSHPatchOperation> allowedOperations) {
        String operationField = request.get("op");
        System.out.println(operationField);

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

        this.path = request.get("path");
        
        String value = request.get("value");

        if (value.trim().isEmpty()) {
            throw new FooSHJsonPatchValueIsEmptyException(parentId);
        }

        if (valueClass == String.class) {
            validateValueAsString(value);
        } else if (valueClass == UUID.class) {
            validateValueAsUUID(value);
        } else {
            throw new FooSHJsonPatchValueException(parentId, valueClass);
        }
    }

    @SuppressWarnings("rawtypes")
    public void validateReplace(Class valueClass) {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() != 3) {
            throw new FooSHJsonPatchFormatException(parentId); 
        }

        this.path = request.get("path");
        
        String value = request.get("value");

        if (value.trim().isEmpty()) {
            throw new FooSHJsonPatchValueIsEmptyException(parentId);
        }

        if (valueClass == String.class) {
            validateValueAsString(value);
        } else if (valueClass == UUID.class) {
            validateValueAsUUID(value);
        } else {
            throw new FooSHJsonPatchValueException(parentId, valueClass);
        }
    }

    public void validateRemove() {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() > 3) {
            if (!keys.containsAll(List.of("op", "path"))) {
                throw new FooSHJsonPatchFormatException(parentId); 
            }
        }

        this.path = request.get("path");
    }

    private void validateValueAsString(String value) {
        if (IdService.isUuid(value).isPresent()) {
            throw new FooSHJsonPatchValueException(parentId, String.class);
        }

        this.value = value;
    }

    private void validateValueAsUUID(String value) {
        if (IdService.isUuid(value).isEmpty()) {
            throw new FooSHJsonPatchValueException(parentId, UUID.class);
        }

        this.value = value;
    }

    // TODO: Fix length bzw. Vergleich
    public boolean isValidPath(List<String> validPaths) {
        boolean isValid = false;
        for (String validPath: validPaths) {
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

    public String getValue() {
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
