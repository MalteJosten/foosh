package com.vs.foosh.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.vs.foosh.api.exceptions.variable.VariableNamePatchRequest;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableDevicesPostRequest;
import com.vs.foosh.api.model.variable.VariableModelPostRequest;
import com.vs.foosh.api.model.variable.VariablePostRequest;
import com.vs.foosh.api.model.variable.VariablePredictionRequest;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.VariableService;

@RestController
@RequestMapping(value = "/api/vars")
public class VariableController {

    ///
    /// Variable Collection
    ///

    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVars() {
        return VariableService.getVariables();
    }

    @PostMapping(value = "/",
            headers  = "batch=true",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postMulitpleVar(@RequestBody List<VariablePostRequest> requests) {
        return VariableService.postMultipleVariables(requests);
    }

    @PostMapping(value = "/",
            headers  = "batch=false",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postSingleVar(@RequestBody VariablePostRequest request) {
        return VariableService.postSingleVariable(request);
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
        return VariableService.deleteVariables();
    }

    ///
    /// Variable
    ///

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVar(@PathVariable("id") String id) {
        return VariableService.getVariable(id);
    }

    @PostMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postVar(@PathVariable("id") String id, @RequestBody VariablePredictionRequest request) {
        return VariableService.startVariablePrediction(id, request);
    }

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVar(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /vars/" + id.replace(" ", "%20") +  "! Either use PATCH to update or DELETE and POST to replace a variable.",
                ListService.getVariableList().getThing(id).getSelfLinks());
    }

    @PatchMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVar(@PathVariable("id") String id, @RequestBody List<Map<String, String>> patchMappings) {
        return VariableService.patchVariable(id, patchMappings);
    }

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVar(@PathVariable("id") String id) {
        return VariableService.deleteVariable(id);
    }


    ///
    /// Devices
    ///

    @GetMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVarDevices(@PathVariable("id") String id) {
        return VariableService.getVariableDevices(id);
    }

    // TODO: Implement Paging
    @PostMapping(value = "/{id}/devices/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postVarDevices(@PathVariable("id") String id, @RequestBody VariableDevicesPostRequest request) {
        return VariableService.postVariableDevices(id, request);
    }

    @PutMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVarDevices(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /vars/" + id.replace(" ", "%20") +  "/devices/! Either use PATCH to update or DELETE and POST to replace the list of associated devices.",
                links);
    }
    
    // TODO: (Implement custom Json Patch)
    @PatchMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVarDevices(@PathVariable("id") String id, @RequestBody List<Map<String, String>> patchMappings) {
        return VariableService.patchVariableDevices(id, patchMappings);
    }

    // TODO: Fix. It is not working!
    @DeleteMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVarDevices(@PathVariable("id") String id) {
        return VariableService.deleteVariableDevices(id);
    }

    ///
    /// Models
    ///

    @GetMapping(value = "/{id}/models/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVarModels(@PathVariable("id") String id) {
        return VariableService.getVariableModels(id);
    }

    @PostMapping(value = "/{id}/models/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postVarModels(@PathVariable("id") String id, @RequestBody VariableModelPostRequest request) {
        return VariableService.postVariableModels(id, request);
    }
    @PutMapping(value = "/{id}/models/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putVarModels(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getThing(id);
        
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getModelLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /vars/" + id + "/models/! Use DELETE and POST instead.",
                links);
    }

    @PatchMapping(value = "/{id}/models/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchVarModels(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException("Not yet implemented!");
    }

    @DeleteMapping(value = "/{id}/models/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteVarModels(@PathVariable("id") String id) {
        return VariableService.deleteVariableModels(id);
    }
}
