package com.vs.foosh.api.model.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.model.misc.ThingType;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPatchRequest;
import com.vs.foosh.api.model.variable.VariableModelPostRequest;
import com.vs.foosh.api.services.helpers.IdService;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueException;

/**
 * This class is a restricted and not fully implemented version of JavaScript Object Notation (JSON) Patch.
 * @see https://www.rfc-editor.org/rfc/rfc6902
 * 
 * In only supports a subset of operations (ADD, REPLACE, and REMOVE) on a variety of routes:
 *     * PATCH /api/devices/{id}:            REPLACE (the {@link AbstractDevice}'s name)
 *     * PATCH /api/variables/{id}:          REPLACE (the {@link Variable}'s name)
 *     * PATCH /api/variables/{id}/devices/: ADD, REMOVE (an {@link AbstractDevice} from the list of linked devices)
 *     * PATCH /api/variables/{id}/models/:  ADD, REPLACE, REMOVE (a link between/to an {@link AbstractPredictionModel})
 *     * PATCH /api/models/{id}:             REPLACE (the {@link AbstractPredictionModel}'s name)
 *     * PATCH /api/models/{id}:             ADD, REPLACE, REMOVE (an entry in an {@link AbstractPredictionModel}'s {@code parameterMappings})
 * 
 * For a detailed description of the fields {@code operation}, {@code path}, and {@code value} as well as the inner workings of JSON Patch, please refer to RFC 6902.
 */
public class FooSHJsonPatch {
    /**
     * An entry of a JSON Patch Document.
     */
    private Map<String, Object> request;

    /**
     * The entry's JSON Patch Operation in form of a {@link FooSHPatchOperation}.
     */
    private FooSHPatchOperation operation;

    /**
     * The entry's JSON Patch path.
     */
    private String path;

    /**
     * The entry's JSON Patch value.
     */
    private Object value;

    /**
     * The ID of the {@link Thing} which is modified by this patch.
     */
    private String parentId;

    /**
     * Return the JSON Patch entry.
     * 
     * @return the field {@code request}
     */
    public Map<String, Object> getRequest() {
        return this.request;
    }

    /**
     * Empty constructor used by Spring to instantiate {@code FooSHJsonPatch} when it is used as a {@link RequestBody}.
     */
    public FooSHJsonPatch() {}

    /**
     * Creates a {@code FooSHJsonPatch}, given a request.
     * 
     * @param request the JSON Patch entry
     */
    public FooSHJsonPatch(Map<String, Object> request) {
        this.request = request;
    }

    /**
     * Set the field {@code parentId}.
     * 
     * @param id the id
     */
    public void setParentId(String id) {
        this.parentId = id;
    }

    /**
     * Return the parent id.
     * 
     * @return the field {@code parentId}
     */
    public String getParentId() {
        return this.parentId;
    }
    
    /**
     * Validate a request by checking if its operation is a valid JSON Patch operation and if we allow this specific operation on the resource.
     * 
     * A {@link FooSHJsonPatchIllegalOperationException} is thrown, if the provided operation is neither a valid JSON Patch operation nor an allowed operation.
     * A {@link FooSHJsonPatchFormatException} is thrown, if there is a general problem with the operation.
     * 
     * @param allowedOperations a {@link List} of allowed JSON Patch operations in form of {@link FooSHPatchOperation}s
     */
    public void validateRequest(List<FooSHPatchOperation> allowedOperations) {
        String operationField = (String) request.get("op");

        // Does the Patch contain a valid operation?
        try {
            this.operation = FooSHPatchOperation.valueOf(operationField.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FooSHJsonPatchIllegalOperationException("The operation '" + operationField + "' is not a valid JSON Patch operation. Please visit https://www.rfc-editor.org/rfc/rfc6902#section-4 for a list of valid operations.");
        } catch (NullPointerException e) {
            throw new FooSHJsonPatchFormatException();
        }

        // Do we allow this operation?
        if (!allowedOperations.contains(operation)) {
            throw new FooSHJsonPatchIllegalOperationException(operation);
        }

    }

    /**
     * Validate a Json Patch request with an ADD operation. 
     */
    @SuppressWarnings("rawtypes")
    public void validateAdd(Class valueClass) {
        Set<String> keys = request.keySet();

        // Does the request contain all necessary fields?
        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() != 3) {
            throw new FooSHJsonPatchFormatException(); 
        }

        // Does a non-empty path exist?
        this.path = (String) request.get("path");

        if (this.path == null || this.path.trim().isEmpty()) {
            throw new FooSHJsonPatchFormatException();
        }
        
        // Validate the value
        Object value = request.get("value");

        if (valueClass == String.class) {
            validateValueAsString(value);
        } else if (valueClass == UUID.class) {
            String uuidValue = (String) value;
            validateValueAsUUID(uuidValue);
        } else if (valueClass == PredictionModelMappingPatchRequest.class) {
            validateValueAsListOfPredictionModelMappingPatchRequests(value);
        } else if (valueClass == VariableModelPostRequest.class) {
            validateValueAsVariableModelPostRequest(value);
        } else {
            throw new FooSHJsonPatchValueException();
        }
    }

    /**
     * Validate a Json Patch request with an REPLACE operation. 
     */
    @SuppressWarnings("rawtypes")
    public void validateReplace(Class valueClass) {
        Set<String> keys = request.keySet();

        // Does the request contain all necessary fields?
        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() != 3) {
            throw new FooSHJsonPatchFormatException(); 
        }

        // Does a non-empty path exist?
        this.path = (String) request.get("path");
        
        if (this.path == null || this.path.trim().isEmpty()) {
            throw new FooSHJsonPatchFormatException();
        }

        // Validate the value
        Object value = request.get("value");

        if (valueClass == String.class) {
            validateValueAsString(value);
        } else if (valueClass == UUID.class) {
            String uuidValue = (String) value;
            validateValueAsUUID(uuidValue);
        } else if (valueClass == PredictionModelMappingPatchRequest.class) {
            validateValueAsListOfPredictionModelMappingPatchRequests(value);
        } else if (valueClass == VariableModelPostRequest.class) {
            validateValueAsVariableModelPostRequest(value);
        } else {
            throw new FooSHJsonPatchValueException();
        }
    }

    /**
     * Validate a Json Patch request with an REMOVE operation. 
     */
    public void validateRemove() {
        Set<String> keys = request.keySet();

        // Does the request contain all necessary fields?
        if (!keys.containsAll(List.of("op", "path", "value")) || keys.size() > 3) {
            if (!keys.containsAll(List.of("op", "path"))) {
                throw new FooSHJsonPatchFormatException(); 
            }
        }

        // Does a non-empty path exist?
        this.path = (String) request.get("path");

        if (this.path == null || this.path.trim().isEmpty()) {
            throw new FooSHJsonPatchFormatException();
        }

        // No need to validate value, since it is absolute in a REMOVE request.
        this.value = null;
    }

    /**
     * Validate the value field if it should contain a {@link String}.
     */
    private void validateValueAsString(Object value) {
        try {
            String stringValue = (String) value;
            if (stringValue.trim().isEmpty()) {
                throw new FooSHJsonPatchValueIsEmptyException();
            }

            if (IdService.isUuid(stringValue).isPresent()) {
                throw new FooSHJsonPatchValueException();
            }

            this.value = stringValue;
        } catch (ClassCastException e) {
            throw new FooSHJsonPatchValueException();
        }
    }

    /**
     * Validate the value field if it should contain a {@link UUID}.
     */
    private void validateValueAsUUID(String value) {
        if (IdService.isUuid(value).isEmpty()) {
            throw new FooSHJsonPatchValueException();
        }

        this.value = value;
    }

    /**
     * Validate the value field if it should contain a {@link PredictionModelMappingPatchRequest}.
     */
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
        } catch (Exception e) {
            throw new FooSHJsonPatchValueException();
        }

    }

    /**
     * Validate the value field if it should contain a {@link VariableModelPostRequest}.
     */
    @SuppressWarnings("unchecked")
    private void validateValueAsVariableModelPostRequest(Object value) {
        try {
            Map<String, Object> valueMapping = (HashMap<String, Object>) value;

            List<ParameterMapping> parameterMappings = new ArrayList<>();
            for (Object paramMappingObject: (List<Object>) valueMapping.get("mappings")) {
                Map<String, String> paramMapping = (HashMap<String, String>) paramMappingObject;
                parameterMappings.add(new ParameterMapping(paramMapping.get("parameter"), paramMapping.get("deviceId"))); 
            }

            this.value = parameterMappings;
        } catch (Exception e) {
            throw new FooSHJsonPatchValueException();
        }
    }

    /**
     * Given a list of valid paths, validate the request's path.
     * 
     * @param validPaths a list of valid paths
     * @param type the {@link ThingType} of the resource that is going to be affected by this request
     * 
     * @return {@code true} if the request's path is valid. Otherwise return {@code false}.
     */
    public boolean isValidPath(List<String> validPaths, ThingType type) {
        boolean isValid = false;

        if (this.path.trim().isEmpty()) {
            return isValid;
        }

        for (String validPath: validPaths) {
            if (validPath.equalsIgnoreCase("uuid")) {
                try {
                    IdService.isUuid(this.path.split("/")[1]).orElseThrow(() -> new IdIsNoValidUUIDException(this.path.split("/")[1], type));
                    isValid = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    isValid = false;
                }
                break;
            }

            if (this.path.equalsIgnoreCase(validPath)) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    /**
     * Return the request's operation.
     * 
     * @return the field {@code operation}
     */
    public FooSHPatchOperation getOperation() {
        return this.operation;
    }

    /**
     * Return the request's path.
     * 
     * @return the field {@code path}
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Return the request's destionation.
     * 
     * @return the last segment of the {@code path}
     */
    public String getDestination() {
        if (this.path.equals("/")) return "";

        return this.path.split("/")[this.path.split("/").length - 1];
    }

    /**
     * Return the request's value.
     * 
     * @return the field {@code value}
     */
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
