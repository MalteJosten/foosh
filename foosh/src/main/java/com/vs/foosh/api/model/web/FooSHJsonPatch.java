package com.vs.foosh.api.model.web;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.github.fge.jsonpatch.JsonPatchException;
import com.vs.foosh.api.services.IdService;

public class FooSHJsonPatch {
    private Map<String, String> request;
    private FooSHPatchOperation operation;
    private String path;
    private String value;

    public FooSHJsonPatch(Map<String, String> request) {
        this.request = request;
    }

    private void validateRequest(List<FooSHPatchOperation> allowedOperations) {
        String operationField = request.get("op");

        // Does the Patch contains a valid operation?
        try {
            operation = FooSHPatchOperation.valueOf(operationField.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FooSHJsonPatchIllegalArgumentException(operationField);
        }

        // Do we allow this operation?
        if (!allowedOperations.contains(operation)) {
            throw new FooSHJsonPatchIllegalOperationException(operation);
        }

    }

    public void validateAdd(Class valueClass) {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() != 3) {
            throw new FooSHJsonPatchFormatException(); 
        }

        this.path = request.get("path");
        
        String value = request.get("value");

        if (value.trim().isEmpty()) {
            throw new FooSHJsonPatchEmptyValueException();
        }

        if (valueClass == String.class) {
            validateValueAsString(value);
        } else if (valueClass == UUID.class) {
            validateValueAsUUID(value);
        } else {
            throw new FooSHJsonPatchValueException(valueClass);
        }
    }

    public void validateReplace(Class valueClass) {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() != 3) {
            throw new FooSHJsonPatchFormatException(); 
        }

        this.path = request.get("path");
        
        String value = request.get("value");

        if (value.trim().isEmpty()) {
            throw new FooSHJsonPatchEmptyValueException();
        }

        if (valueClass == String.class) {
            validateValueAsString(value);
        } else if (valueClass == UUID.class) {
            validateValueAsUUID(value);
        } else {
            throw new FooSHJsonPatchValueException(valueClass);
        }
    }

    public void validateRemove() {
        Set<String> keys = request.keySet();

        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() > 3) {
            if (!keys.containsAll(List.of("op", "path"))) {
                throw new FooSHJsonPatchFormatException(); 
            }
        }

        this.path = request.get("path");
    }

    private void validateValueAsString(String value) {
        if (IdService.isUuid(value)) {
            throw new FoosHJsonPatchValueException(String.class);
        }

        this.value = value;
    }

    private void validateValueAsUUID(String value) {
        if (!IdService.isUuid(value)) {
            throw new FoosHJsonPatchValueException(UUID.class);
        }

        this.value = value;
    }

    
}
