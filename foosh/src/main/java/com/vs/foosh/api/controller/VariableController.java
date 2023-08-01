package com.vs.foosh.api.controller;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.misc.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsEmptyException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNullException;
import com.vs.foosh.api.exceptions.variable.VariableNamePatchRequest;
import com.vs.foosh.api.model.device.DeviceResponseObject;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableDevicesPostRequest;
import com.vs.foosh.api.model.variable.VariablePostRequest;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.FooSHPatchOperation;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.IdService;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

// TODO: Extract logic into dedicated service(s)
@RestController
@RequestMapping(value = "/api/vars")
public class VariableController {

    ///
    /// Variable Collection
    ///

    // TODO: Implement Paging
    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVars() {

        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getVariableList().getLinks("self"));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // TODO: Implement Paging
    @PostMapping(value = "/",
            headers  = "batch=true",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postMulitpleVar(@RequestBody List<VariablePostRequest> requests) {
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

        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getVariableList().getLinks("self"));
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    // TODO: Implement Paging
    @PostMapping(value = "/",
            headers  = "batch=false",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postSingleVar(@RequestBody VariablePostRequest request) {
        ListService.getVariableList().addThing(processPostRequest(request));
        ListService.getVariableList().updateLinks();

        PersistentDataService.saveVariableList();

        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getVariableList().getLinks("self"));
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);

    }

    private Variable processPostRequest(VariablePostRequest request) {
        // Name validation
        // Is there a field called 'name'?
        if (request.getName() == null) {
            throw new VariableCreationException("Cannot create variable without a name! Please provide a field 'name'.");
        }

        // Is this field not empty?
        if (request.getName().trim().isEmpty()) {
            throw new VariableCreationException("Cannot create variable without a name! The field 'name' is empty.");
        }

        // TODO: Use IdService
        // Does this field contain an UUID?
        try {
            UUID.fromString(request.getName());
            throw new VariableCreationException("Cannot create variable! Variables must not be an UUID.");
        } catch (IllegalArgumentException e) { }

        // Check for duplicates
        if (!ListService.getVariableList().isUniqueName(request.getName(), null)) {
            throw new VariableCreationException("Cannot create variable(s)! The name '" + request.getName() + "' is already taken.");
        }

        String name = request.getName().toLowerCase();

        return new Variable(name, new ArrayList<>(), new ArrayList<>());
    }

    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVars() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /vars/! Use DELETE and POST to replace the list of variables.",
                ListService.getVariableList().getLinks("self"));
    }

    @PatchMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVars(@RequestBody List<VariableNamePatchRequest> request) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on /vars/! Either use PATCH on /vars/{id} to update the variable's name or DELETE and POST to replace the list of variables.",
                ListService.getVariableList().getLinks("self"));
    }

    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVars() {
        ListService.getVariableList().clearList();

        Map<String, String> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getVariableListLink().toString());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variables", ListService.getVariableList().getList());
        responseBody.put("_links", linkBlock);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    ///
    /// Variable
    ///

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVar(@PathVariable("id") String id) {

        Variable variable = ListService.getVariableList().getThing(id);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variable", variable.getDisplayRepresentation().getVariable());
        responseBody.put("_links", variable.getAllLinks());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postVar() {
        // TODO: Start prediction.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVar(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /vars/{id}! Either use PATCH to update or DELETE and POST to replace a variable.",
                ListService.getVariableList().getThing(id).getSelfLinks());
    }

    // TODO: (Implement Paging)
    @PatchMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVar(@PathVariable("id") String id, @RequestBody List<Map<String, String>> patchMappings) {
        // check whether id is an UUID and whether there is a device with the given id
        ListService.getVariableList().checkIfIdIsPresent(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, String> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.REPLACE));
            patch.validateReplace(String.class);

            patches.add(patch);
        }    

        Variable variable = ListService.getVariableList().getThing(id);
        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("name");
            if (!patch.hasPath(pathSegments.toArray(new String[0]), true)) {
                throw new FooSHJsonPatchIllegalArgumentException("You can only edit the field 'name'!");
            }

            patchVariableName(id, patch.getValue());
        }

        PersistentDataService.saveVariableList();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variable", variable.getDisplayRepresentation().getVariable());
        responseBody.put("_links", variable.getAllLinks());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVar(@PathVariable("id") String id) {
        ListService.getVariableList().deleteThing(id);

        PersistentDataService.saveVariableList();

        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getVariableList().getLinks("self"));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private boolean patchVariableName(String id, String patchName) {
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

    ///
    /// Devices
    ///

    // TODO: Implement Paging
    @GetMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVarDevices(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getThing(id);
        List<DeviceResponseObject> devices = new ArrayList<>();

        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getDeviceList().getThing(deviceId.toString()).getDisplayRepresentation().getDevice());
        }

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", devices);
        responseBody.put("_links", links);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // TODO: Implement Paging
    @PostMapping(value = "/{id}/devices/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postVarDevices(@PathVariable("id") String id, @RequestBody VariableDevicesPostRequest request) {
        // Remove duplicates
        List<UUID> deviceIds = new ArrayList<>(new HashSet<>(request.getDevices()));

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

        Variable variable = ListService.getVariableList().getThing(id);

        variable.setDevices(deviceIds);

        PersistentDataService.saveAll();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variable", variable.getDisplayRepresentation().getVariable());
        responseBody.put("_links", variable.getAllLinks());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }

    @PutMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVarDevices(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /vars/{id}/devices/! Either use PATCH to update or DELETE and POST to replace the list of associated devices.",
                links);
    }
    
    // TODO: (Implement custom Json Patch)
    @PatchMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVarDevices(@PathVariable("id") String id, @RequestBody List<Map<String, String>> patchMappings) {
        // check whether there is a device with the given id
        ListService.getVariableList().checkIfIdIsPresent(id);

        Variable variable = ListService.getVariableList().getThing(id);

        // Convert patchMappings to FooSHJsonPatches
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

        // Check for correct path
        for (FooSHJsonPatch patch : patches) {
            List<String> pathSegments;
            switch (patch.getOperation()) {
                case ADD:
                    pathSegments = List.of();
                    break;
                case REPLACE:
                    // use "uuid" as a placeholder for checking path
                    pathSegments = List.of("uuid");
                    break;
                case REMOVE:
                    // use "uuid" as a placeholder for checking path
                    pathSegments = List.of("uuid");
                    break;
                default:
                    throw new FooSHJsonPatchIllegalOperationException(patch.getOperation());
            }
            if (!patch.hasPath(pathSegments.toArray(new String[0]), false)) {
                throw new FooSHJsonPatchIllegalArgumentException("You can only add a device under '/' and/or replace/remove a device using its UUID with '/<uuid>'!");
            }

            // Execute operation
            List<UUID> devices = variable.getDeviceIds();
            String value = patch.getValue();
            switch (patch.getOperation()) {
                case ADD:
                    if (!devices.contains(UUID.fromString(value))) {
                        devices.add(UUID.fromString(value));
                    }
                    break;
                case REPLACE:
                    if (!devices.contains(UUID.fromString(value))) {
                        throw new FooSHJsonPatchIllegalArgumentException("Could not replace device since it is not part of the device collection.");
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

            variable.unregister();
            variable.setDevices(devices);
            variable.register();
            variable.updateLinks();

            PersistentDataService.saveAll();
        }

        List<DeviceResponseObject> devices = new ArrayList<>();
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", devices);
        responseBody.put("_links", links);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deletVarDevices(@PathVariable("id") String id) {
        ListService.getVariableList().getThing(id).clearDevices();

        PersistentDataService.saveAll();

        Variable variable = ListService.getVariableList().getThing(id);
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
}
