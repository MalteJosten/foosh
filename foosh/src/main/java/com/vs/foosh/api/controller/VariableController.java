package com.vs.foosh.api.controller;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vs.foosh.api.exceptions.HttpMappingNotAllowedException;
import com.vs.foosh.api.model.VariableList;
import com.vs.foosh.api.model.HttpAction;
import com.vs.foosh.api.model.LinkEntry;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;

@RestController
@RequestMapping(value="/api/")
public class VariableController {

    ///
    /// Environment Variables
    ///

    @GetMapping("vars/")
    public ResponseEntity<Object> getVars() {
        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("variables", VariableList.getVariables()),
                VariableList.getLinks("self"),
                HttpStatus.OK);
    }

    @PostMapping("vars/")
    public ResponseEntity<Object> postVars() {
        // TODO: Allow creation of mulitple variables at once?
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.POST, List.of("application/json")));

        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /vars/! Please use POST on /vars/{id} instead.",
                links);
    }

    @PutMapping("vars/")
    public ResponseEntity<Object> putVars() {
        // TODO: Only on SINGLE variable level.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PatchMapping("vars/")
    public ResponseEntity<Object> patchVars() {
        // TODO: Only on SINGLE variable level.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @DeleteMapping("vars/")
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

    @GetMapping("vars/{id}")
    public ResponseEntity<Object> getVar() {
        // TODO: Retrieve environment variable
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PostMapping("vars")
    public ResponseEntity<Object> postVar() {
        // TODO: What exactly needs/can be included when creating new variable? 
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PutMapping("vars/{id}")
    public ResponseEntity<Object> putVar() {
        // TODO: Allow replacement only when all fields are present (?)
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PatchMapping("vars/{id}")
    public ResponseEntity<Object> patchVar() {
        // TODO: What can be updated? Depends on POST.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @DeleteMapping("vars/{id}")
    public ResponseEntity<Object> deleteVar() {
        // TODO: Delete variable.
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
