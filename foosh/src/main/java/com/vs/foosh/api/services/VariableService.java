package com.vs.foosh.api.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalPathException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsNullException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.variable.MalformedVariableModelPostRequestException;
import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.DeviceResponseObject;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.misc.ThingType;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
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
import com.vs.foosh.api.services.helpers.IdService;
import com.vs.foosh.api.services.helpers.ListService;

/**
 * A {@link Service} that provides functionalities for accessing and modifying (elements of) the {@link VariableList}.
 */
@Service
public class VariableService {

    /**
     * Return the contents of {@link VariableList}.
     * 
     * @return {@link #respondWitVariables(HttpStatus.OK)}
     */
    public static ResponseEntity<Object> getVariables() {
        return respondWithVariables(HttpStatus.OK);
    }

    /**
     * Given a list of {@link VariablPostRequest}s, add one or multiple {@link Variable} to {@link VariableList}.
     * 
     * @param requests the {@link List} with elements of type {@link VariablePostRequest} containing {@link Variable}s to add
     * 
     * @return {@link #respondWitVariables(HttpStatus.CREATED)}
     */
    public static ResponseEntity<Object> addVariables(List<VariablePostRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new VariableCreationException("Cannot create variables! Please provide a collection of variable names.", HttpStatus.BAD_REQUEST);
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

    /**
     * Add a {@link Variable} to {@link VariableList}.
     * 
     * @param request the {@link Variable}s to add
     * 
     * @return {@link #respondWitVariables(HttpStatus.CREATED)}
     */
    public static ResponseEntity<Object> addVariable(VariablePostRequest request) {
        ListService.getVariableList().addThing(processPostRequest(request));
        ListService.getVariableList().updateLinks();

        PersistentDataService.saveVariableList();
        
        return respondWithVariables(HttpStatus.CREATED);
    }

    /**
     * Process a {@link VariablePostRequest} to create a {@link Variable}.
     * 
     * @param request the {@link VariablePostRequest} containing the information needed for creating a {@link Variable}
     * 
     * @return the created {@link Variable}
     */
    private static Variable processPostRequest(VariablePostRequest request) {
        // Name validation
        // Is there a field called 'name'?
        if (request.name() == null) {
            throw new VariableCreationException("Cannot create variable without a name! Please provide a field 'name'.", HttpStatus.BAD_REQUEST);
        }

        // Is this field not empty?
        if (request.name().trim().isEmpty()) {
            throw new VariableCreationException("Cannot create variable without a name! The field 'name' is empty.", HttpStatus.BAD_REQUEST);
        }

        // Is the name an UUID?
        if (IdService.isUuid(request.name()).isPresent()) {
            throw new VariableCreationException("Cannot create variable! Variables must not be an UUID.", HttpStatus.BAD_REQUEST);
        }

        // Check for duplicates
        if (!ListService.getVariableList().isValidName(request.name(), null)) {
            throw new VariableCreationException("Cannot create variable(s)! The name '" + request.name() + "' is already taken.", HttpStatus.CONFLICT);
        }

        String name = request.name().toLowerCase();

        return new Variable(name, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Delete the contents of {@link VariableList}.
     * 
     * @return {@link #respondWitVariables(HttpStatus.OK)}
     */
    public static ResponseEntity<Object> deleteVariables() {
        ListService.getVariableList().clearList();

        PersistentDataService.saveAll();

        return respondWithVariables(HttpStatus.OK);
    }

    /**
     * Construct and return the {@link VariableDisplayRepresentation} for all variables of {@link VariableLIst} and the necessary hypermedia links.
     * 
     * @param status the {@link HttpStatus} of the response
     * 
     * @return the display representations and hypermedia links as a {@link ResponseEntity} with status {@code status}
     */
    private static ResponseEntity<Object> respondWithVariables(HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("variables", ListService.getVariableList().getDisplayListRepresentation());
        responseBody.put("_links", ListService.getVariableList().getLinks("self"));

        return new ResponseEntity<>(responseBody, status);
    }

    /**
     * Given an identifier ({@code id} or {@code name}), return a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * 
     * @return {@link #respondWitVariable(id)}
     */
    public static ResponseEntity<Object> getVariable(String id) {
        return respondWithVariable(id);
    }

    /**
     * Given an identifier and a prediction request, calculate, (execute,) and return results.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name} to generate the prediction for
     * @param request the {@link VariablePredictionRequest} containing needed information to perform the prediction
     * 
     * @return the response as a {@link ResponseEntity} with response code 200
     */
    public static ResponseEntity<Object> startVariablePrediction(String id, VariablePredictionRequest request) {
        Variable variable = ListService.getVariableList().getThing(id);

        request.validate(id);

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

    /**
     * Execute a {@link FooSHJsonPatch} on a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} to change the {@code name} of
     * @param patchMappings a JSON Patch document
     * 
     * @return {@link #respondWitVariable(id)}
     */
    public static ResponseEntity<Object> patchVariable(String id, List<Map<String, Object>> patchMappings) {
        Variable variable = ListService.getVariableList().getThing(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, Object> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.REPLACE));
            patch.validateReplace(String.class);

            patches.add(patch);
        }    

        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("/name");
            if (!patch.isValidPath(pathSegments, ThingType.VARIABLE)) {
                throw new FooSHJsonPatchIllegalPathException("You can only edit the field 'name'!");
            }

            patchVariableName(id, (String) patch.getValue());
        }

        variable.updateLinks();

        PersistentDataService.saveVariableList();
        
        return respondWithVariable(id);
    }

    /**
     * Patch a {@link Variable}'s {@code name}.
     * 
     * @param id the {@link Variable}'s {@code id} to change the {@code name} of
     * 
     * @return {@code true} if patching was successful. Otherwise, return {@code false}.
     */
    private static boolean patchVariableName(String id, String patchName) {
        UUID uuid = IdService.isUuid(id).get();

        if (patchName == null) {
            throw new FooSHJsonPatchValueIsNullException();
        }

        if (patchName.trim().isEmpty() || patchName.equals("")) {
            throw new FooSHJsonPatchValueIsEmptyException();
        }

        if (ListService.getVariableList().isValidName(patchName, uuid)) {
            ListService.getVariableList().getThing(id).setName(patchName);
            return true;
        }
        return false;
    }

    /**
     * Remove a {@link Variable} form {@link VariableList}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * 
     * @return {@link #respondWitVariables(HttpStatus.OK)}
     */
    public static ResponseEntity<Object> deleteVariable(String id) {
        ListService.getVariableList().getThing(id).unregisterFromSubject();
        ListService.getVariableList().deleteThing(id);

        PersistentDataService.saveVariableList();

        return respondWithVariables(HttpStatus.OK);
    }

    /**
     * Construct and return the {@link VariableDisplayRepresentation} of a {@link Variable} and the necessary hypermedia links.
     * 
     * @param id the {@link Variable}'s {@code id}
     * 
     * @return the display representation and hypermedia links as a {@link ResponseEntity} with status {@code HttpStatus.OK}
     */
    private static ResponseEntity<Object> respondWithVariable(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variable", variable.getDisplayRepresentation().getVariable());
        responseBody.put("_links"  , variable.getAllLinks());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * Return a {@link Variable}'s linked {@link AbstractDevice}s.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * 
     * @return {@link #respondWithDevices()}
     */
    public static ResponseEntity<Object> getVariableDevices(String id) {
        Variable variable = ListService.getVariableList().getThing(id);
        List<DeviceResponseObject> devices = new ArrayList<>();

        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getDeviceList().getThing(deviceId.toString()).getDisplayRepresentation().getDevice());
        }

        return respondWithDevices(variable);
    }

    /**
     * Add a list of {@link AbstractDevice}s to the list of linked devices ({@code deviceIds}) of a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * @param request the {@link VariableDevicesPostRequest} which contains needed information
     * 
     * @return {@link #respondWithDevices()}
     */
    public static ResponseEntity<Object> postVariableDevices(String id, VariableDevicesPostRequest request) {
        Variable variable = ListService.getVariableList().getThing(id);

        if (request.deviceIds() == null) {
            throw new VariableDevicePostException(
                id,
                "The list of device-IDs is missing. Please provide a field 'deviceIds' with a list of device-IDs.",
                HttpStatus.BAD_REQUEST);
        }

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
                    "Could not find a device with ID '" + deviceId + "'. Aborting.",
                    HttpStatus.NOT_FOUND
                );
            }
        }

        variable.setDeviceIds(deviceIds);
        variable.updateLinks();

        PersistentDataService.saveAll();

        return respondWithDevices(variable);
    }

    /**
     * Edit the list of {@link AbstractDevice}s ({@code deviceIds}) of a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * @param patchMappings a JSON Patch document
     * 
     * @return {@link #respondWithDevices()}
     */
    public static ResponseEntity<Object> patchVariableDevices(String id, List<Map<String, Object>> patchMappings) {
        Variable variable = ListService.getVariableList().getThing(id);

        // Convert patchMappings to FooSHJsonPatches
        List<FooSHJsonPatch> patches = convertDevicesPatchMappings(id, patchMappings);


        for (FooSHJsonPatch patch: patches) {
            executeDevicesPatch(variable, patch);

            List<UUID> devices = variable.getDeviceIds();
            variable.unregisterFromSubject();
            variable.setDeviceIds(devices);
            variable.registerToSubject();
            variable.updateLinks();

            PersistentDataService.saveAll();
        }

        List<DeviceResponseObject> devices = new ArrayList<>();

        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getDeviceList().getThing(deviceId.toString()).getDisplayRepresentation().getDevice());
        }

        return respondWithDevices(variable);
    }

    /**
     * Convert a JSON Patch document for editing the list of linked {@AbstractDevice}s to a list of {@link FooSHJsonPatch} requests.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * @param patchMappings a JSON Patch document
     * 
     * @return a {@link List} with elements of type {@link FooSHJsonPatch}
     */
    private static List<FooSHJsonPatch> convertDevicesPatchMappings(String id, List<Map<String, Object>> patchMappings) {
        Variable variable = ListService.getVariableList().getThing(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, Object> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.setParentId(id);

            // To comply with RFC 6902, we need to make sure, that all instructions are valid.
            // Otherwise, we must not execute any patch instruction.
            patch.validateRequest(List.of(FooSHPatchOperation.ADD, FooSHPatchOperation.REMOVE));

            switch (patch.getOperation()) {
                case ADD:
                    patch.validateAdd(UUID.class);

                    checkForCorrectDevicesPatchPath(patch);

                    if (variable.getDeviceIds().contains(UUID.fromString((String) patch.getValue()))) {
                        throw new FooSHJsonPatchOperationException(
                            variable.getId(),
                            variable.getVarDeviceLinks(),
                            "You can only use add on devices that are not yet present in the list!",
                            HttpStatus.BAD_REQUEST);
                    }

                    break;
                case REMOVE:
                    patch.validateRemove();

                    checkForCorrectDevicesPatchPath(patch);

                    if (!variable.getDeviceIds().contains(UUID.fromString(patch.getDestination()))) {
                        throw new FooSHJsonPatchOperationException(
                            variable.getId(),
                            variable.getVarDeviceLinks(),
                            "You can only use remove on devices that are present in the list!",
                            HttpStatus.NOT_FOUND);
                    }

                    break;
                default:
                    throw new FooSHJsonPatchIllegalOperationException(patch.getOperation());
            }

            patches.add(patch);
        }

        return patches;
    } 

    /**
     * Check whether a given {@link FooSHJsonPatch} concerning linked devices contains a correct path.
     * 
     * @param patch the {@link FooSHJsonPatch} to check the path of
     */
    private static void checkForCorrectDevicesPatchPath(FooSHJsonPatch patch) {
        List<String> pathSegments;
        switch (patch.getOperation()) {
            case ADD:
                pathSegments = List.of("/");
                break;
            case REMOVE:
                // use "uuid" as a placeholder
                pathSegments = List.of("uuid");
                break;
            default:
                throw new FooSHJsonPatchIllegalOperationException(patch.getOperation());
        }

        if (!patch.isValidPath(pathSegments, ThingType.VARIABLE)) {
            throw new FooSHJsonPatchIllegalPathException("You can only add a device under '/' and/or remove a device using its UUID with '/{id}'!");
        }
    }

    /**
     * Execute a given {@link FooSHJsonPatch} on a {@link Variable}.
     * 
     * @param variable the {@link Variable} to execute the patch on
     * @param patch the {@link FooSHJsonPatch} to execute
     */
    private static void executeDevicesPatch(Variable variable, FooSHJsonPatch patch) {
        List<UUID> devices = variable.getDeviceIds();

        String value = (String) patch.getValue();

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
                    throw new FooSHJsonPatchIllegalPathException("Could not replace device since it is not part of the device collection.");
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

    /**
     * Clear the list of linked {@link AbstractDevice}'s (@code deviceIds) of a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * 
     * @return {@link #respondWithDevices()}
     */
    public static ResponseEntity<Object> deleteVariableDevices(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        variable.unregisterFromSubject();
        variable.clearDeviceIds();

        PersistentDataService.saveAll();
    
        return respondWithDevices(variable);
    }

    /**
     * Construct and return the list of linked {@link AbstractDevice}s and the necessary hypermedia links.
     * 
     * @param variable the {@link Variable} to return the list of linked devices from
     * 
     * @return the display representations and hypermedia links as a {@link ResponseEntity} with status {@code HttpStatus.OK}
     */
    private static ResponseEntity<Object> respondWithDevices(Variable variable) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        List<AbstractDevice> devices = new ArrayList<>();
        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getDeviceList().getThing(deviceId.toString()));
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", devices);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * Return a {@link Variable}'s linked {@link AbstractPredictionModel}s.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * 
     * @return {@link #respondWithModels()}
     */
    public static ResponseEntity<Object> getVariableModels(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        return respondWithModels(variable);
    }

    /**
     * Add a {@link AbstractPredictionModel} to the list of linked prediction models ({@code modelIds}) of a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * @param request the {@link VariableModelPostRequest} which contains needed information
     * 
     * @return {@link #respondWithModels()}
     */
    public static ResponseEntity<Object> addVariableModel(String id, VariableModelPostRequest request) {
        Variable variable = ListService.getVariableList().getThing(id);

        if (request.modelId() == null) {
            throw new MalformedVariableModelPostRequestException(
                id,
                "Please provide a field named 'modelId'!"
            );
        }

        IdService.isUuid(request.modelId().toString()).orElseThrow(() -> new MalformedVariableModelPostRequestException(
            id,
            "Please provide a UUID for the field 'modelId'!"));

        // Check whether the user provided a valid predictionModel identifier
        AbstractPredictionModel linkToModel = ListService.getPredictionModelList().getThing(request.modelId().toString());

        // Check if we already have a model with the same ID
        if (variable.getModelIds().contains(request.modelId())) {
            throw new MalformedVariableModelPostRequestException(
                id,
                "The model '" + linkToModel.getName() + "' (" + linkToModel.getId() + ") is already linked to the variable '" + variable.getName() + "' (" + variable.getId() + ").",
                HttpStatus.CONFLICT
            );
        }

        // Check whether the user provided a list of parameter mappings
        if (request.mappings() == null) {
            throw new MalformedVariableModelPostRequestException(
                id,
                "Please provide a field named 'mappings' containing a list of Parameter Mappings!");
        }

        // Rewriting post request to be able to forward it to the PredictionModelService.postMappings(...)
        // It is going to be handled as if the user did a POST /models/{modelId}/mappings/
        PredictionModelService.postMappings(request.modelId().toString(), new PredictionModelMappingPostRequest(variable.getId(), request.mappings()));

        return respondWithModels(variable);
    }

    /**
     * Edit the list of {@link AbstractPredictionModel}s ({@code modelIds}) of a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * @param patchMappings a JSON Patch document
     * 
     * @return {@link #respondWithModels()}
     */
    public static ResponseEntity<Object> patchVariableModels(String id, List<Map<String, Object>> patchMappings) {
        Variable variable = ListService.getVariableList().getThing(id);

        List<FooSHJsonPatch> patches = convertModelsPatchMappings(variable, patchMappings);

        for (FooSHJsonPatch patch: patches) {
            checkForCorrectModelsPatchPath(patch);
            checkForCorrectModelPresence(variable, patch);
        }

        reformatAndForwardModelPatches(variable, patches);

        variable.updateLinks();

        PersistentDataService.saveAll();

        return respondWithModels(variable);
    }

    /**
     * Convert a JSON Patch document for editing the list of linked {@link AbstractPredictionModel}s to a list of {@link FooSHJsonPatch} requests.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * @param patchMappings a JSON Patch document
     * 
     * @return a {@link List} with elements of type {@link FooSHJsonPatch}
     */
    private static List<FooSHJsonPatch> convertModelsPatchMappings(Variable variable, List<Map<String, Object>> patchMappings) {
        List<FooSHJsonPatch> patches = new ArrayList<>();

        for (Map<String, Object> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.setParentId(variable.getId().toString());
            patch.validateRequest(List.of(FooSHPatchOperation.ADD, FooSHPatchOperation.REPLACE, FooSHPatchOperation.REMOVE));

            switch (patch.getOperation()) {
                case ADD:
                    patch.validateAdd(VariableModelPostRequest.class);
                    break;
                case REPLACE:
                    patch.validateReplace(VariableModelPostRequest.class);
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

    /**
     * Check whether a given {@link FooSHJsonPatch} concerning linked prediction models contains a correct path.
     * 
     * @param patch the {@link FooSHJsonPatch} to check the path of
     */
    private static void checkForCorrectModelsPatchPath(FooSHJsonPatch patch) {
        List<String> allowedPathSegments = List.of("uuid");
        if (!patch.isValidPath(allowedPathSegments, ThingType.VARIABLE)) {
            throw new FooSHJsonPatchIllegalPathException("You can only edit an individual mapping regarding one model at a time. Use '/{modelId}' as the path.");
        }
    }

    /**
     * Check whether the {@link AbstractPredictionModel} addressed by the patch is already linked to the {@link Variable}.
     * 
     * @param variable the {@link Variable} to check the list of linked {@link AbstractPredictionModel}s of 
     * @param patch the {@link FooSHJsonPatch} to check the path of
     */
    private static void checkForCorrectModelPresence(Variable variable, FooSHJsonPatch patch) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(patch.getDestination());

        if (patch.getOperation() == FooSHPatchOperation.REPLACE) {
            if (!variable.getModelIds().contains(model.getId())) {
                List<LinkEntry> links = variable.getModelLinks();
                if (links.isEmpty()) {
                    links.addAll(variable.getSelfLinks());
                }

                throw new FooSHJsonPatchOperationException(
                        variable.getId(),
                        links,
                        "You can only replace mappings which exist. Use the operation 'add' to add new mappings.",
                        HttpStatus.NOT_FOUND);

            }
        }

        if (patch.getOperation() == FooSHPatchOperation.REMOVE) {
            if (!variable.getModelIds().contains(model.getId())) {
                List<LinkEntry> links = variable.getModelLinks();
                if (links.isEmpty()) {
                    links.addAll(variable.getSelfLinks());
                }

                throw new FooSHJsonPatchOperationException(
                        variable.getId(),
                        links,
                        "You can only remove mappings which exist.",
                        HttpStatus.NOT_FOUND);

            }
        }

    }
    
    /**
     * Reformats a list of {@link FooSHJsonPatch}s concerning the patch of a linked {@link AbstractPredictionModel} in a way, so it can be forwarded to the prediction model and handled by itself.
     * 
     * @param variable the {@link Variable} to change the link of
     * @param patches the list with elements of type {@link FooSHJsonPatch}
     */
    private static void reformatAndForwardModelPatches(Variable variable, List<FooSHJsonPatch> patches) {
        for (FooSHJsonPatch patch: patches) {
            switch (patch.getOperation()) {
                case ADD:
                    reformatAndForwardPatch(variable, patch);
                    break;
                case REPLACE:
                    reformatAndForwardPatch(variable, patch);
                    break;
                case REMOVE:
                    PredictionModelService.deleteMapping(ListService.getPredictionModelList().getThing(patch.getDestination()), variable.getId());
                    break;
                default:
                    throw new FooSHJsonPatchIllegalOperationException(patch.getOperation());
            }
        }

        PersistentDataService.saveAll();
    }

    /**
     * Reformat a single {@link FooSHJsonPatch} concerning the patch of a linked {@link AbstractPredictionModel} in a way, so it can be forwarded to the prediction model and handled by itself.
     * 
     * @param variable the {@link Variable} to change the link of
     * @param patch the {@link FooSHJsonPatch}
     */
    @SuppressWarnings("unchecked")
    private static void reformatAndForwardPatch(Variable variable, FooSHJsonPatch patch) {
        Map<String, Object> forwardHashMap = new HashMap<>();
        List<ParameterMapping> oldPatchValue = (List<ParameterMapping>) patch.getValue();
        forwardHashMap.put("op", patch.getOperation().toString());
        forwardHashMap.put("path", "/" + variable.getId());

        List<Map<String, String>> paramMappings = new ArrayList<>();
        for (ParameterMapping parameterMapping: oldPatchValue) {
            Map<String, String> hashParamMapping = parameterMapping.getAsMap();
            paramMappings.add(hashParamMapping);
        }

        forwardHashMap.put("value", paramMappings);

        PredictionModelService.patchMappings(patch.getDestination().toString(), List.of(forwardHashMap));
    }

    /**
     * Clear the list of linked {@link AbstractPredictionModel}'s (@code modelIds) of a {@link Variable}.
     * 
     * @param id the {@link Variable}'s {@code id} or {@code name}
     * 
     * @return {@link #respondWithModels()}
     */
    public static ResponseEntity<Object> deleteVariableModels(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        variable.clearModelIds();

        PersistentDataService.saveAll();

        return respondWithModels(variable);
    }

    /**
     * Construct and return the list of linked {@link AbstractPredictionModel}s and the necessary hypermedia links.
     * 
     * @param variable the {@link Variable} to return the list of linked prediction models from
     * 
     * @return the display representations and hypermedia links as a {@link ResponseEntity} with status {@code HttpStatus.OK}
     */
    private static ResponseEntity<Object> respondWithModels(Variable variable) {
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
