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
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPostRequest;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PredictionModelService;

@RestController
@RequestMapping(value = "/api/models")
public class PredictionModelController {

    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsGet() {
        return PredictionModelService.getModels();
    }

    @PostMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPost() {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /models/! You can only use the pre-defined models.",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPut() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /models/! Use PATCH on /models/{id} instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @PatchMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPatch() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on /models/! Use PATCH on /models/{id} instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsDelete() {
        throw new HttpMappingNotAllowedException(
                "You cannot use DELETE on /models/! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }


    //
    // Model
    //

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelGet(@PathVariable("id") String id) {
        return PredictionModelService.getModel(id);
    }

    @PostMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPost(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /models/" + id.replace(" ", "%20") +  "! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getThing(id).getSelfLinks());
    }

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPut(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /models/" + id.replace(" ", "%20") +  "! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getThing(id).getSelfLinks());
    }

    // TODO: Implement custom Json Patch
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPatch(@PathVariable("id") String id, @RequestBody List<Map<String, String>> patchMappings) {
        return PredictionModelService.patchModel(id, patchMappings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> modelDelete(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use DELETE on /models/" + id.replace(" ", "%20") +  "! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }
    
    ///
    /// Mapping
    ///

    // TODO: Implement Paging
    @GetMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getModelMapping(@PathVariable("id") String id) {
        return PredictionModelService.getMappings(id);
    }

    // TODO: Implement Paging
    @PostMapping(value = "/{id}/mappings/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postModelMapping(@PathVariable("id") String id, @RequestBody PredictionModelMappingPostRequest request) {
        return PredictionModelService.postMappings(id, request);
    }

    @PutMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putModelMapping(@PathVariable("id") String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getMappingLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /models/" + id.replace(" ", "%20") +  "/mappings/! Use either PATCH to edit or DELETE and POST to replace the current mapping.",
                links);
    }
    
    // TODO: (Implement custom Json Patch)
    @PatchMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchModelMapping(@PathVariable("id") String id, @RequestBody List<Map<String, String>> patchMappings) {
        throw new HttpMappingNotAllowedException(
                "Not implemented",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @DeleteMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteModelMapping(@PathVariable("id") String id) {
        return PredictionModelService.deleteMappings(id);
    }

}
