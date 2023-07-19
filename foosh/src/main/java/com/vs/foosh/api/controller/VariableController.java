package com.vs.foosh.api.controller;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableList;
import com.vs.foosh.api.model.variable.VariablePostRequest;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;

@RestController
@RequestMapping(value = "/api/")
public class VariableController {

    ///
    /// Environment Variables
    ///

    @GetMapping(value = "vars/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVars() {

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", VariableList.getDisplayListRepresentation()),
                VariableList.getLinks("self"),
                HttpStatus.OK);
    }

    @PostMapping(value = "vars/",
            headers  = "batch=true",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postMulitpleVar(@RequestBody List<VariablePostRequest> requests) {
        System.out.println("multiple");
        if (requests == null || requests.isEmpty()) {
            throw new VariableCreationException("Cannot create variables! Please provide a collection 'variables.");
        }

        List<Variable> variables = new ArrayList<>();
        for (VariablePostRequest subRequest: requests) {
            variables.add(processPostRequest(subRequest));
        }

        VariableList.setVariables(variables);
        VariableList.updateVariableLinks();

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", VariableList.getDisplayListRepresentation()),
                VariableList.getLinks("self"),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "vars/",
            headers  = "batch=false",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postSingleVar(@RequestBody VariablePostRequest request) {
        System.out.println("single");
        VariableList.pushVariable(processPostRequest(request));
        VariableList.updateVariableLinks();

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", VariableList.getDisplayListRepresentation()),
                VariableList.getLinks("self"),
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
        if (!VariableList.isUniqueName(request.getName())) {
            throw new VariableCreationException("Cannot create variable(s)! The name '" + request.getName() + "' is already taken.");
        }

        String name = request.getName().toLowerCase();

        // Validate device IDs
        List<UUID> deviceIds = new ArrayList<>();
        if (request.getDevices() != null) {
            for (UUID deviceId: request.getDevices()) {
                try {
                    DeviceList.checkIfIdIsPresent(deviceId.toString());
                } catch (DeviceIdNotFoundException e) {
                    throw new VariableCreationException("Cannot create variable! Could not find device with id " + deviceId + ".");
                }
            }
            deviceIds.addAll(request.getDevices());
        }

        // TODO: Check if given modelId(s) are valid
        List<UUID> modelIds = new ArrayList<>();

        return new Variable(name, deviceIds, modelIds);
    }

    @PutMapping(value = "vars/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVars() {
        // TODO: Only on SINGLE variable level.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PatchMapping(value = "vars/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVars() {
        // TODO: Only on SINGLE variable level.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @DeleteMapping(value = "vars/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVars() {
        VariableList.clearVariables();

        Map<String, String> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getVariableListLink().toString());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variables", VariableList.getVariables());
        responseBody.put("links", linkBlock);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    ///
    /// Environment Variable
    ///

    @GetMapping(value = "vars/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVar(@PathVariable("id") String id) {
        Variable variable = VariableList.getVariable(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());
        links.addAll(variable.getModelLinks());
        links.addAll(variable.getExtLinks());

        return HttpResponseBuilder.buildResponse(variable, links, HttpStatus.OK);
    }

    @PostMapping(value = "vars",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postVar() {
        // TODO: What exactly needs/can be included when creating new variable? 
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PutMapping(value = "vars/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVar() {
        // TODO: Allow replacement only when all fields are present (?)
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PatchMapping(value = "vars/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVar() {
        // TODO: What can be updated? Depends on POST.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @DeleteMapping(value = "vars/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVar() {
        // TODO: Delete variable.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
