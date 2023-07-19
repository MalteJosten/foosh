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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vs.foosh.api.exceptions.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.VariableCreationException;
import com.vs.foosh.api.model.DeviceList;
import com.vs.foosh.api.model.Variable;
import com.vs.foosh.api.model.VariableList;
import com.vs.foosh.api.model.VariablePostRequest;
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
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postVars(@RequestBody VariablePostRequest request) {
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

        // TODO: Check for duplicates
        if (!VariableList.isUniqueName(request.getName())) {
            throw new VariableCreationException("Cannot create variable! The name is already taken.");
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

        VariableList.pushVariable(new Variable(name, deviceIds, modelIds));

        VariableList.updateVariableLinks();

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", VariableList.getDisplayListRepresentation()),
                VariableList.getLinks("self"),
                HttpStatus.CREATED);

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
    public ResponseEntity<Object> getVar() {
        // TODO: Retrieve environment variable
        return new ResponseEntity<Object>(HttpStatus.OK);
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
