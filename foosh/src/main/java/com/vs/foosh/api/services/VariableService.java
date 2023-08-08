package com.vs.foosh.api.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.variable.MalformedVariableModelPostRequestException;
import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsEmptyException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNullException;
import com.vs.foosh.api.model.device.DeviceResponseObject;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPostRequest;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableDevicesPostRequest;
import com.vs.foosh.api.model.variable.VariableModelPostRequest;
import com.vs.foosh.api.model.variable.VariablePostRequest;
import com.vs.foosh.api.model.variable.VariablePredictionRequest;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.FooSHPatchOperation;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.model.web.SmartHomePostResult;

public class VariableService {

    ///
    /// Variable Collection
    ///

    public static ResponseEntity<Object> getVariables() {
        return respondWithVariables(HttpStatus.OK);
    }

    public static ResponseEntity<Object> addVariables(List<VariablePostRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new VariableCreationException("Cannot create variables! Please provide a collection of variable names.");
        }

        List<Variable> variables = new ArrayList<>();
        for (VariablePostRequest subRequest: requests) {
            variables.add(processPostRequest(subRequest));
        }

        ListService.getVariableList().setList(variables);
        ListService.getVariableList().updateLinks();

        PersistentDataService.saveVariableList();

        return respondWithVariables(HttpStatus.CREATED);

    }

    public static ResponseEntity<Object> addVariable(VariablePostRequest request) {
        ListService.getVariableList().addThing(processPostRequest(request));
        ListService.getVariableList().updateLinks();

        PersistentDataService.saveVariableList();
        
        return respondWithVariables(HttpStatus.CREATED);
    }


    private static Variable processPostRequest(VariablePostRequest request) {
        // Name validation
        // Is there a field called 'name'?
        if (request.name() == null) {
            throw new VariableCreationException("Cannot create variable without a name! Please provide a field 'name'.");
        }

        // Is this field not empty?
        if (request.name().trim().isEmpty()) {
            throw new VariableCreationException("Cannot create variable without a name! The field 'name' is empty.");
        }

        // Is the name an UUID?
        if (IdService.isUuid(request.name()).isPresent()) {
            throw new VariableCreationException("Cannot create variable! Variables must not be an UUID.");
        }

        // Check for duplicates
        if (!ListService.getVariableList().isUniqueName(request.name(), null)) {
            throw new VariableCreationException("Cannot create variable(s)! The name '" + request.name() + "' is already taken.");
        }

        String name = request.name().toLowerCase();

        return new Variable(name, new ArrayList<>(), new ArrayList<>());
    }

    public static ResponseEntity<Object> deleteVariables() {
        ListService.getVariableList().clearList();

        PersistentDataService.saveAll();

        return respondWithVariables(HttpStatus.OK);
    }

    // TODO: Implement paging
    private static ResponseEntity<Object> respondWithVariables(HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("variables", ListService.getVariableList().getDisplayListRepresentation());
        responseBody.put("_links", ListService.getVariableList().getLinks("self"));

        return new ResponseEntity<>(responseBody, status);
    }

    ///
    /// Variable
    ///

    public static ResponseEntity<Object> getVariable(String id) {
        return respondWithVariable(id);
    }

    public static ResponseEntity<Object> startVariablePrediction(String id, VariablePredictionRequest request) {
        Variable variable = ListService.getVariableList().getThing(id);
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(request.modelId());

        if (!variable.getModelIds().contains(model.getId())) {
            throw new MalformedVariablePredictionRequest(
                id,
                "The model with ID '" + request.modelId() + "' is not yet linked to the variable '" + variable.getName() + "' (" + variable.getId() + ")!");
        }

        List<SmartHomeInstruction> smartHomeInstructions = model.makePrediction(variable.getId(), request.value());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variable", variable.getName());
        responseBody.put("value", request.value());
        responseBody.put("instructions", smartHomeInstructions);

        if (request.execute()) {
            List<SmartHomePostResult> responses = SmartHomeService.getSmartHomeInstructionExecutor().sendAndExecuteSmartHomeInstructions(smartHomeInstructions);

            responseBody.put("responses", responses);
        }

        return new ResponseEntity<Object>(responseBody, HttpStatus.OK);

    }

    public static ResponseEntity<Object> patchVariable(String id, List<Map<String, String>> patchMappings) {
        // check whether id is an UUID and whether there is a device with the given id
        ListService.getVariableList().checkIfIdIsPresent(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, String> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.REPLACE));
            patch.validateReplace(String.class);

            patches.add(patch);
        }    

        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("name");
            if (!patch.hasPath(pathSegments.toArray(new String[0]), true)) {
                throw new FooSHJsonPatchIllegalArgumentException("You can only edit the field 'name'!");
            }

            patchVariableName(id, patch.getValue());
        }

        PersistentDataService.saveVariableList();
        
        return respondWithVariable(id);
    }

    private static boolean patchVariableName(String id, String patchName) {
        UUID uuid = IdService.isUuid(id).get();

        if (patchName == null) {
            throw new VariableNameIsNullException(uuid);
        }

        if (patchName.trim().isEmpty() || patchName.equals("")) {
            throw new VariableNameIsEmptyException(uuid);
        }

        // check whether there is a variable with the given id
        ListService.getVariableList().checkIfIdIsPresent(id);
        if (ListService.getVariableList().isUniqueName(patchName, uuid)) {
            ListService.getVariableList().getThing(id).setName(patchName);
            return true;
        }
        return false;

    }

    public static ResponseEntity<Object> deleteVariable(String id) {
        ListService.getVariableList().getThing(id).unregisterFromSubject();
        ListService.getVariableList().deleteThing(id);

        PersistentDataService.saveVariableList();

        return respondWithVariable(id);
    }

    private static ResponseEntity<Object> respondWithVariable(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variable", variable.getDisplayRepresentation().getVariable());
        responseBody.put("_links"  , variable.getAllLinks());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    ///
    /// Devices
    ///

    public static ResponseEntity<Object> getVariableDevices(String id) {
        Variable variable = ListService.getVariableList().getThing(id);
        List<DeviceResponseObject> devices = new ArrayList<>();

        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getDeviceList().getThing(deviceId.toString()).getDisplayRepresentation().getDevice());
        }

        return respondWithVariableAndDevices(variable);
    }

    // TODO: Implement Paging
    public static ResponseEntity<Object> postVariableDevices(String id, VariableDevicesPostRequest request) {
        Variable variable = ListService.getVariableList().getThing(id);

        // Do we already have stored devices?
        // If that's the case, we disallow POST
        if (!variable.getDeviceIds().isEmpty()) {
            List<LinkEntry> links = new ArrayList<>();
            links.addAll(variable.getSelfLinks());
            links.addAll(variable.getVarDeviceLinks());
            throw new HttpMappingNotAllowedException(
                "The variable '" + variable.getName() + "' (" + variable.getId() + ") already has devices set as dependencies. Use PATCH instead to edit the dependencies.",
                links);
        }

        // Remove duplicates
        List<UUID> deviceIds = new ArrayList<>(new HashSet<>(request.deviceIds()));

        // Check whether the given device IDs match actual device IDs
        for (UUID deviceId: deviceIds) {
            if(!IdService.isUuidInList(deviceId, ListService.getDeviceList().getList())) {
                throw new VariableDevicePostException(
                    id,
                    deviceId,
                    "Could not find a device with ID '" + deviceId + "'. Aborting.",
                    HttpStatus.BAD_REQUEST
                );
            }
        }

        variable.setDevices(deviceIds);
        variable.updateLinks();

        PersistentDataService.saveAll();

        return respondWithVariableAndDevices(variable);
    }

    public static ResponseEntity<Object> patchVariableDevices(String id, List<Map<String, String>> patchMappings) {
        Variable variable = ListService.getVariableList().getThing(id);

        // Convert patchMappings to FooSHJsonPatches
        List<FooSHJsonPatch> patches = convertPatchMappings(patchMappings);

        // To comply with RFC 6902, we need to make sure, that all instructions are valid.
        // Otherwise, we must not execute any patch instruction.
        for (FooSHJsonPatch patch : patches) {
            checkForCorrectPatchPath(patch);
        }

        for (FooSHJsonPatch patch: patches) {
            executePatch(variable, patch);

            List<UUID> devices = variable.getDeviceIds();
            variable.unregisterFromSubject();
            variable.setDevices(devices);
            variable.registerToSubject();
            variable.updateLinks();

            PersistentDataService.saveAll();
        }

        List<DeviceResponseObject> devices = new ArrayList<>();

        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getDeviceList().getThing(deviceId.toString()).getDisplayRepresentation().getDevice());
        }

        return respondWithVariableAndDevices(variable);
    }

    private static List<FooSHJsonPatch> convertPatchMappings(List<Map<String, String>> patchMappings) {
        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, String> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.ADD));

            // TODO: Implement replace, remove
            switch (patch.getOperation()) {
                case ADD:
                    patch.validateAdd(UUID.class);
                    break;
                case REPLACE:
                    patch.validateReplace(UUID.class);
                    break;
                case REMOVE:
                    patch.validateRemove();
                    break;
                default:
                    throw new FooSHJsonPatchIllegalOperationException(patch.getOperation());
            }

            patches.add(patch);
        }

        return patches;
    }    

    private static void checkForCorrectPatchPath(FooSHJsonPatch patch) {
        List<String> pathSegments;
        switch (patch.getOperation()) {
            case ADD:
                pathSegments = List.of();
                break;
            case REPLACE:
                // use "uuid" as a placeholder
                pathSegments = List.of("uuid");
                break;
            case REMOVE:
                // use "uuid" as a placeholder
                pathSegments = List.of("uuid");
                break;
            default:
                throw new FooSHJsonPatchIllegalOperationException(patch.getOperation());
        }

        if (!patch.hasPath(pathSegments.toArray(new String[0]), true)) {
            throw new FooSHJsonPatchIllegalArgumentException(
                    "You can only add a device under '/' and/or replace/remove a device using its UUID with '/{id}'!");
        }
    }

    private static void executePatch(Variable variable, FooSHJsonPatch patch) {
        List<UUID> devices = variable.getDeviceIds();

        String value = patch.getValue();

        switch (patch.getOperation()) {
            case ADD:
                if (!devices.contains(UUID.fromString(value))) {
                    if (!IdService.isIdentifierInList(value, ListService.getDeviceList().getList())) {
                        throw new DeviceIdNotFoundException(value);
                    }

                    devices.add(UUID.fromString(value));
                }
                break;
            case REPLACE:
                if (!devices.contains(UUID.fromString(value))) {
                    if (!IdService.isIdentifierInList(value, ListService.getDeviceList().getList())) {
                        throw new DeviceIdNotFoundException(value);
                    }
                    throw new FooSHJsonPatchIllegalArgumentException(
                            "Could not replace device since it is not part of the device collection.");
                }

                devices.remove(UUID.fromString(patch.getDestination()));
                devices.add(UUID.fromString(value));
                break;
            case REMOVE:
                devices.remove(UUID.fromString(patch.getDestination()));
                break;
            default:
                throw new FooSHJsonPatchIllegalOperationException(patch.getOperation());
        }

    }

    public static ResponseEntity<Object> deleteVariableDevices(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        variable.unregisterFromSubject();
        variable.clearDevices();

        PersistentDataService.saveAll();
    
        return respondWithVariableAndDevices(variable);
    }

    // TODO: Implement Paging
    private static ResponseEntity<Object> respondWithVariableAndDevices(Variable variable) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variable", variable.getDisplayRepresentation().getVariable());
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    ///
    /// Models
    ///
    
    public static ResponseEntity<Object> getVariableModels(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        return respondWithVariableAndModels(variable);
    }

    public static ResponseEntity<Object> addVariableModel(String id, VariableModelPostRequest request) {
        Variable variable = ListService.getVariableList().getThing(id);

        // Check whether the user provided a valid predictionModel identifier
        AbstractPredictionModel linkToModel = ListService.getPredictionModelList().getThing(request.modelId().toString());

        // Check if we already have a model with the same ID
        if (variable.getModelIds().contains(request.modelId())) {
            throw new MalformedVariableModelPostRequestException(
                id,
                "The model '" + linkToModel.getName() + "' (" + linkToModel.getId() + ") is already linked to the variable '" + variable.getName() + "' (" + variable.getId() + ")."
            );
        }

        // Rewriting post request to be able to forward it to the PredictionModelService.postMappings(...)
        // It is going to be handled as if the user did a POST /models/{modelId}/mappings/
        PredictionModelService.postMappings(request.modelId().toString(), new PredictionModelMappingPostRequest(variable.getId(), request.mappings()));

        return respondWithVariableAndModels(variable);
    }

    public static ResponseEntity<Object> deleteVariableModels(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        variable.clearModels();

        return respondWithVariableAndModels(variable);
    }

    private static ResponseEntity<Object> respondWithVariableAndModels(Variable variable) {
        List<Thing> models = new ArrayList<>();
        for (UUID modelId: variable.getModelIds()) {
            for (AbstractPredictionModel model: ListService.getPredictionModelList().getList()) {
                if (model.getId().equals(modelId)) {
                    models.add(model.getDisplayRepresentation().getModel());
                }
            }
        }

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getModelLinks());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("models", models);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
