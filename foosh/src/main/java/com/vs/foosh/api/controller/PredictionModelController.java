package com.vs.foosh.api.controller;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

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
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.services.ListService;

@RestController
@RequestMapping(value = "/api/models")
public class PredictionModelController {

    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsGet() {
        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("prediction_models", ListService.getPredictionModelList().getDisplayListRepresentation());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getPredictionModelList().getLinks("self"));

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPost() {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /models/! Use GET on /models/ to get the list of available models.",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPut() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/! Use DELETE and POST to replace the list of devices.",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @PatchMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPatch() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on /devices/! Either use PATCH on /devices/{id} to update the device's name or DELETE and POST to replace the list of devices.",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsDelete() {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /models/! Use GET on /models/ to get the list of available models.",
                ListService.getPredictionModelList().getLinks("self"));
    }


    //
    // Model
    //

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelGet(@PathVariable("id") String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("prediction_model", model.getDisplayRepresentation().getModel());
        responseBody.put("_links", model.getAllLinks());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPost(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /models/! Use GET on /models/ to get the list of available models.",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPut(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/{id}! Either use PATCH to update or DELETE and POST to replace a device.",
                ListService.getPredictionModelList().getThing(id).getSelfLinks());
    }

    // TODO: Implement custom Json Patch
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPatch(@PathVariable("id") String id, @RequestBody Map<String, String> requestBody) {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /models/! Use GET on /models/ to get the list of available models.",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> modelDelete(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /models/! Use GET on /models/ to get the list of available models.",
                ListService.getPredictionModelList().getLinks("self"));
    }
}
