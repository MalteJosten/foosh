package com.vs.foosh.api.model.web;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchEmptyValueException;
import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchValueException;
import com.vs.foosh.api.services.IdService;

public class FooSHJsonPatch {
    private Map<String, String> request;
    private FooSHPatchOperation operation;
    private String path;
    private String value;

    public Map<String, String> getRequest() {
        return this.request;
    }

    public FooSHJsonPatch() {}

    public FooSHJsonPatch(Map<String, String> request) {
        this.request = request;
    }

    public void validateRequest(List<FooSHPatchOperation> allowedOperations) {
        String operationField = request.get("op");

        // Does the Patch contain a valid operation?
        try {
            operation = FooSHPatchOperation.valueOf(operationField.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FooSHJsonPatchIllegalArgumentException("The operation '" + operationField.toLowerCase() + "' is not a valid Json Patch operation. Please visit https://www.rfc-editor.org/rfc/rfc6902#section-4 for a list of valid operations.");
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

    @SuppressWarnings("rawtypes")
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
        if (IdService.isUuid(value).isPresent()) {
            throw new FooSHJsonPatchValueException(String.class);
        }

        this.value = value;
    }

    private void validateValueAsUUID(String value) {
        if (IdService.isUuid(value).isEmpty()) {
            throw new FooSHJsonPatchValueException(UUID.class);
        }

        this.value = value;
    }

    // TODO: Fix length bzw. Vergleich
    public boolean hasPath(String[] desiredPath, boolean includeLast) {
        String[] pathSegments = this.path.split("/");

        for(String s: pathSegments) {
            System.out.println(s);
        }

        if (includeLast) {
            if (pathSegments.length != desiredPath.length) {
                System.err.println("not the same length");
                return false;
            }
        } else {
            if ((pathSegments.length - 1) != desiredPath.length) {
                System.err.println("not the same length");
                return false;
            }
        }

        int lastIndex = pathSegments.length - 1;
        if (!includeLast) {
            lastIndex--;
        }

        for(int i = 0; i <= lastIndex; i++) {
            if (!pathSegments[i].equalsIgnoreCase(desiredPath[i])) {
                return false;
            }
        }

        return true;
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
        builder.append("operation:\t" + operation + "\n");
        builder.append("path:\t\t"    + path + "\n");
        builder.append("value:\t\t"   + value);

        return builder.toString();
    }
}
