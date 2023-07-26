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

import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.variable.BatchVariableNameException;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsEmptyException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNullException;
import com.vs.foosh.api.exceptions.variable.VariableNamePatchRequest;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableDevicesPostRequest;
import com.vs.foosh.api.model.variable.VariableList;
import com.vs.foosh.api.model.variable.VariablePostRequest;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.IdService;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

@RestController
@RequestMapping(value = "/api/vars")
public class VariableController {

    ///
    /// Variable Collection
    ///

    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVars() {

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation()),
                ListService.getVariableList().getLinks("self"),
                HttpStatus.OK);
    }

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

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation()),
                ListService.getVariableList().getLinks("self"),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/",
            headers  = "batch=false",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postSingleVar(@RequestBody VariablePostRequest request) {
        ListService.getVariableList().pushVariable(processPostRequest(request));
        ListService.getVariableList().updateVariableLinks();

        PersistentDataService.saveVariableList();

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation()),
                ListService.getVariableList().getLinks("self"),
                HttpStatus.CREATED);

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
                "You cannot use PUT on /vars/! Either use PATCH to update or DELETE and POST to replace the list of variables.",
                ListService.getVariableList().getLinks("self"));
    }

    @PatchMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVars(@RequestBody List<VariableNamePatchRequest> request) {
        if (patchBatchVariableName(request)) {
            PersistentDataService.saveVariableList();

            return HttpResponseBuilder.buildResponse(
                    new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation()),
                    ListService.getVariableList().getLinks("self"),
                    HttpStatus.OK);
        } else {
            throw new BatchVariableNameException();
        }
    }

    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVars() {
        ListService.getVariableList().clearVariables();

        Map<String, String> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getVariableListLink().toString());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variables", ListService.getVariableList().getVariables());
        responseBody.put("links", linkBlock);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    ///
    /// Variable
    ///

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVar(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getVariable(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getExtLinks());

        return HttpResponseBuilder.buildResponse(variable, links, HttpStatus.OK);
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

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVar(@PathVariable("id") String id, @RequestBody Map<String, String> requestBody) {
        if (patchVariableName(new VariableNamePatchRequest(id, requestBody.get("name")))) {
            PersistentDataService.saveVariableList();

            Variable variable = ListService.getVariableList().getVariable(id);

            List<LinkEntry> links = new ArrayList<>();
            links.addAll(variable.getSelfLinks());
            links.addAll(variable.getExtLinks());

            return HttpResponseBuilder.buildResponse(variable, links, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("Could not patch name for variable '" + id + "' !'", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVar(@PathVariable("id") String id) {
        ListService.getVariableList().deleteVariable(id);

        PersistentDataService.saveVariableList();

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", ListService.getVariableList().getDisplayListRepresentation()),
                ListService.getVariableList().getLinks("self"),
                HttpStatus.OK);
    }

    private boolean patchVariableName(VariableNamePatchRequest request) {
        UUID uuid;

        // Is the provided id a valid UUID?
        try {
            uuid = UUID.fromString(request.getId());
        } catch (IllegalArgumentException e) {
            throw new IdIsNoValidUUIDException(request.getId());
        }

        if (request.getName() == null) {
            throw new VariableNameIsNullException(uuid);
        }

        if (request.getName().trim().isEmpty() || request.getName().equals("")) {
            throw new VariableNameIsEmptyException(uuid);
        }

        // check whether there is a variable with the given id
        ListService.getVariableList().checkIfIdIsPresent(request.getId());
        if (ListService.getVariableList().isUniqueName(request.getName(), uuid)) {
            ListService.getVariableList().getVariable(request.getId().toString()).setName(request.getName());
            return true;
        }
        return false;
    }

    private boolean patchBatchVariableName(List<VariableNamePatchRequest> batchRequest) {
        VariableList oldVariableList = ListService.getVariableList();

        for(VariableNamePatchRequest request: batchRequest) {
            if (!patchVariableName(request)) {
                ListService.getVariableList().clearVariables();
                ListService.setVariableList(oldVariableList);
                return false;
            } 
        }

        return true;

    }

    ///
    /// Devices
    ///

    @GetMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVarDevices(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getVariable(id);
        List<Thing> devices = new ArrayList<>();

        for (UUID deviceId: variable.getDeviceIds()) {
            devices.add(ListService.getAbstractDeviceList().getDeviceById(deviceId.toString()));
        }

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        return HttpResponseBuilder.buildResponse(devices, "devices", links, HttpStatus.OK);
    }

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

        PersistentDataService.saveVariableList();

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());

        return HttpResponseBuilder.buildResponse(variable, links, HttpStatus.CREATED);

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
    
    // TODO: Implement method
    @PatchMapping(value = "/{id}/devices/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVarDevices(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "Not yet implemented",
                ListService.getVariableList().getVariable(id).getSelfLinks());
    }
    
    // TODO: Implement method
    @DeleteMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deletVarDevices(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "Not yet implemented",
                ListService.getVariableList().getVariable(id).getSelfLinks());
    }

    ///
    /// Models
    ///
}
