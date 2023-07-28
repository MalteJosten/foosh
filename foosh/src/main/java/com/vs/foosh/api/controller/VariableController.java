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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsEmptyException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNullException;
import com.vs.foosh.api.exceptions.variable.VariableNamePatchRequest;
import com.vs.foosh.api.exceptions.variable.VariablePatchException;
import com.vs.foosh.api.model.device.AbstractDeviceResponseObject;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableDevicesPostRequest;
import com.vs.foosh.api.model.variable.VariablePostRequest;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
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

        ListService.getVariableList().setVariables(variables);
        ListService.getVariableList().updateVariableLinks();

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
        ListService.getVariableList().pushVariable(processPostRequest(request));
        ListService.getVariableList().updateVariableLinks();

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
        ListService.getVariableList().clearVariables();

        Map<String, String> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getVariableListLink().toString());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variables", ListService.getVariableList().getVariables());
        responseBody.put("_links", linkBlock);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    ///
    /// Variable
    ///

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVar(@PathVariable("id") String id) {

        Variable variable = ListService.getVariableList().getVariable(id);
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
                ListService.getVariableList().getVariable(id).getSelfLinks());
    }

    // TODO: (Implement Paging)
    // TODO: Implement custom Json Patch
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVar(@PathVariable("id") String id, @RequestBody List<FooSHJsonPatch> patch) {
        if (patchVariableName(id, patch)) {
            PersistentDataService.saveVariableList();

            Variable variable = ListService.getVariableList().getVariable(id);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("variable", variable.getDisplayRepresentation().getVariable());
            responseBody.put("_links", variable.getAllLinks());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("Could not patch name for variable '" + id + "' !'", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVar(@PathVariable("id") String id) {
        ListService.getVariableList().deleteVariable(id);

        PersistentDataService.saveVariableList();

        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getVariableList().getLinks("self"));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // TODO: May need a rework to only work with one resource or maybe on the entire collection...
    private boolean patchVariableName(String id, JsonPatch patch) {
        UUID uuid;

        // TODO: Use IdService
        // Is the provided id a valid UUID?
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IdIsNoValidUUIDException(id);
        }

        try {
            Variable oldVariable = ListService.getVariableList().getVariable(id);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(oldVariable, JsonNode.class));
            Variable newVariable = objectMapper.treeToValue(patched, Variable.class);

            // make sure that only the name was modified
            List<String> modifications = newVariable.getModifiedFields(oldVariable);

            // No changes.
            if (modifications.size() == 0) {
                return true;
            }

            // More than one field has been changed.
            // Not desired behavior. Abort!
            if (modifications.size() > 1) {
                // TODO: Error response
                return false;
            }

            // Exactly one field has been changed.
            if (modifications.size() == 1) {
                // Was the changed field the 'name' field?
                // We do not allow any other field to be altered.
                if (!modifications.get(0).equals("name")) {
                    return false;
                }

                if (newVariable.getName() == null) {
                    throw new VariableNameIsNullException(uuid);
                }

                if (newVariable.getName().trim().isEmpty() || newVariable.getName().equals("")) {
                    throw new VariableNameIsEmptyException(uuid);
                }

                // check whether there is a variable with the given id
                ListService.getVariableList().checkIfIdIsPresent(id);
                if (ListService.getVariableList().isUniqueName(newVariable.getName(), uuid)) {
                    ListService.getVariableList().getVariable(id).setName(newVariable.getName());
                    return true;
                }
            }

            return false;
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            throw new VariablePatchException(uuid);
        }

    }

    ///
    /// Devices
    ///

    // TODO: Implement Paging
    @GetMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVarDevices(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getVariable(id);
        List<AbstractDeviceResponseObject> devices = new ArrayList<>();

        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getAbstractDeviceList().getDeviceById(deviceId.toString()).getDisplayRepresentation().getDevice());
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
            if(!IdService.isUuidInList(deviceId, ListService.getAbstractDeviceList().getDevices())) {
                throw new VariableDevicePostException(
                    id,
                    deviceId,
                    "Could not find a device with ID '" + deviceId + "'. Aborting.",
                    HttpStatus.BAD_REQUEST
                );
            }
        }

        Variable variable = ListService.getVariableList().getVariable(id);

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
        Variable variable = ListService.getVariableList().getVariable(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /vars/{id}/devices/! Either use PATCH to update or DELETE and POST to replace the list of associated devices.",
                links);
    }
    
    // TODO: Allow patching?
    // TODO: (Implement custom Json Patch)
    @PatchMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVarDevices(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getVariable(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on /vars/{id}/devices/! Use PATCH on /vars/{id} instead.",
                links);
    }

    @DeleteMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deletVarDevices(@PathVariable("id") String id) {
        ListService.getVariableList().getVariable(id).clearDevices();

        PersistentDataService.saveAll();

        Variable variable = ListService.getVariableList().getVariable(id);
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
