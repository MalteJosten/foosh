package com.vs.foosh.api.controller;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPostRequest;
import com.vs.foosh.api.model.predictionModel.VariableParameterMapping;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

@RestController
@RequestMapping(value = "/api/models")
public class PredictionModelController {

    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsGet() {
        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("predictionModels", ListService.getPredictionModelList().getDisplayListRepresentation());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getPredictionModelList().getLinks("self"));

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
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
                "You cannot use PUT on /models/! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }

    @PatchMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPatch() {
        throw new HttpMappingNotAllowedException(
                "Not yet implemented",
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
    public ResponseEntity<Object> modelPatch(@PathVariable("id") String id, @RequestBody Map<String, String> requestBody) {
        throw new HttpMappingNotAllowedException(
                "Not implemented",
                ListService.getPredictionModelList().getLinks("self"));
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
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<VariableParameterMapping> mappings = model.getAllMappings();

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getDeviceLinks());
        links.addAll(model.getVariableLinks());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mappings", mappings);
        responseBody.put("_links", links);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // TODO: Implement Paging
    @PostMapping(value = "/{id}/mappings/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postModelMapping(@PathVariable("id") String id, @RequestBody PredictionModelMappingPostRequest request) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        request.validate(id, ListService.getVariableList().getThing(request.getVariableId().toString()).getDeviceIds());

        model.setMapping(request.getVariableId(), request.getMappings()); 
        model.updateLinks();

        PersistentDataService.saveAll();

        VariableParameterMapping mapping = model.getParameterMapping(request.getVariableId());

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getDeviceLinks());
        links.addAll(ListService.getVariableList().getThing(request.getVariableId().toString()).getSelfStaticLinks("variable"));
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mapping", mapping);
        responseBody.put("_links", links);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    // TODO: Add /models/{id}/mappings/ links to linkBlock
    @PutMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putModelMapping(@PathVariable("id") String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getDeviceLinks());

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
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        model.deleteMapping();

        PersistentDataService.savePredictionModelList();

        List<VariableParameterMapping> mappings = model.getAllMappings();

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getDeviceLinks());
        links.addAll(model.getVariableLinks());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mappings", mappings);
        responseBody.put("_links", links);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
