package com.vs.foosh.api.controller;

import java.net.URI;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vs.foosh.api.model.EnvironmentVariableList;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;

@RestController
public class EnvironmentVariableController {

    ///
    /// Environment Variables
    ///

    @GetMapping("/api/vars/")
    public ResponseEntity<Object> getVars() {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getVariableListLink());

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", EnvironmentVariableList.getVariables()),
                linkBlock,
                HttpStatus.OK);
    }

    @PostMapping("/api/vars/")
    public ResponseEntity<Object> postVars() {
        // TODO: Allow creation of mulitple variables at once?
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PutMapping("/api/vars/")
    public ResponseEntity<Object> putVars() {
        // TODO: Only on SINGLE variable level.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PatchMapping("/api/vars/")
    public ResponseEntity<Object> patchVars() {
        // TODO: Only on SINGLE variable level.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @DeleteMapping("/api/vars/")
    public ResponseEntity<Object> deleteVars() {
        EnvironmentVariableList.clearVariables();

        Map<String, String> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getVariableListLink().toString());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("variables", EnvironmentVariableList.getVariables());
        responseBody.put("links", linkBlock);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    ///
    /// Environment Variable
    ///

    @GetMapping("/api/var/{id}")
    public ResponseEntity<Object> getVar() {
        // TODO: Retrieve environment variable
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PostMapping("/api/var")
    public ResponseEntity<Object> postVar() {
        // TODO: What exactly needs/can be included when creating new variable? 
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PutMapping("/api/var/{id}")
    public ResponseEntity<Object> putVar() {
        // TODO: Allow replacement only when all fields are present (?)
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PatchMapping("/api/var/{id}")
    public ResponseEntity<Object> patchVar() {
        // TODO: What can be updated? Depends on POST.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @DeleteMapping("/api/var/{id}")
    public ResponseEntity<Object> deleteVar() {
        // TODO: Delete variable.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
