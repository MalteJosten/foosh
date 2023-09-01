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
import com.vs.foosh.api.services.PredictionModelService;
import com.vs.foosh.api.services.helpers.ListService;

/**
 * A {@link RestController} that handles HTTP requests for the routes {@code /api/models/}, {@code /api/models/{id}}, and {@code /api/models/mappings/}.
 */
@RestController
@RequestMapping(value = "/api/models")
public class PredictionModelController {

    private static final String ROUTE = "/api/models/";

    /**
     * Handle incoming {@code GET} requests on route {@code /api/models/} using {@link GetMapping}.
     * 
     * Retrieve the list of {@link AbstractPredicitonModel}s.
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsGet() {
        return PredictionModelService.getModels();
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/models/} using {@link PostMapping}.
     * 
     * @apiNote Using {@code POST} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PostMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPost() {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on " + ROUTE + "! Please use the pre-defined model(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/models/} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPut() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + "! Use the pre-defined model(s) and/or use PATCH on /models/{id} instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/models/} using {@link PatchMapping}.
     * 
     * @apiNote Using {@code PATCH} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PatchMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsPatch() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on " + ROUTE + "! Use PATCH on /models/{id} instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/models/} using {@link DeleteMapping}.
     * 
     * @apiNote Using {@code DELETE} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelsDelete() {
        throw new HttpMappingNotAllowedException(
                "You cannot use DELETE on " + ROUTE + "! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getLinks("self"));
    }


    //
    // Model
    //

    /**
     * Handle incoming {@code GET} requests on route {@code /api/models/{id}} using {@link GetMapping}.
     * 
     * Retrieve the a {@link AbstractPredicitonModel}s.
     * 
     * @param id the {@link AbstractPredictionModel}'s identifier (either its {@code name} or {@code id})
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelGet(@PathVariable("id") String id) {
        return PredictionModelService.getModel(id);
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/models/{id}} using {@link PostMapping}.
     * 
     * @apiNote Using {@code POST} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PostMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPost(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use POST on " + ROUTE + id.replace(" ", "%20") +  "! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getThing(id).getSelfLinks());
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/models/{id}} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPut(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + id.replace(" ", "%20") +  "! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getThing(id).getSelfLinks());
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/models/{id}} using {@link PatchMapping}.
     * 
     * Change the {@code name} of an {@link AbstractPredictionModel}.
     * 
     * @param id the {@link AbstractPredictionModel}'s {@code id}
     * @param patchMappings a Json Patch Document
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PatchMapping(value = "/{id}",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelPatch(@PathVariable("id") String id, @RequestBody List<Map<String, Object>> patchMappings) {
        return PredictionModelService.patchModel(id, patchMappings);
    }

    /**
     * Handle incoming {@code GET} requests on route {@code /api/models/{id}} using {@link GetMapping}.
     * 
     * Retrieve the a {@link AbstractPredicitonModel}s.
     * 
     * @param id the {@link AbstractPredictionModel}'s identifier (either its {@code name} or {@code id})
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> modelDelete(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use DELETE on " + ROUTE + id.replace(" ", "%20") +  "! Use PATCH instead, to edit the parameter mapping(s).",
                ListService.getPredictionModelList().getThing(id).getSelfLinks());
    }
    
    ///
    /// Mapping
    ///

    /**
     * Handle incoming {@code GET} requests on route {@code /api/models/{id}/mappings/} using {@link GetMapping}.
     * 
     * Retrieve the parameter mappings for an {@link AbstractPredicitonModel}.
     * 
     * @param id the {@link AbstractPredictionModel}'s identifier (either its {@code name} or {@code id})
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @GetMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelMappingsGet(@PathVariable("id") String id) {
        return PredictionModelService.getMappings(id);
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/models/{id}/mappings/} using {@link PostMapping}.
     * 
     * Add a parameter mapping to {@code parameterMappings} of an {@link AbstractPredictionModel}.
     * 
     * @param id the {@link AbstractPredictionModel}'s identifier (either its {@code name} or {@code id})
     * @param request the {@link PredictionModelMappingPostRequest} containing the new parameter mapping
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PostMapping(value = "/{id}/mappings/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelMappingsPost(@PathVariable("id") String id, @RequestBody PredictionModelMappingPostRequest request) {
        return PredictionModelService.postMappings(id, request);
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/models/{id}/mappings/} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PutMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelMappingsPut(@PathVariable("id") String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getMappingLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + id.replace(" ", "%20") +  "/mappings/! Use either PATCH to edit or DELETE and POST to replace the current mapping.",
                links);
    }
    
    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/models/{id}/mappings/} using {@link PatchMapping}.
     * 
     * Edit the {@code parameterMappings} of an {@link AbstractPredictionModel}.
     * 
     * @param id the {@link AbstractPredictionModel}'s {@code id}
     * @param patchMappings a Json Patch Document
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PatchMapping(value = "/{id}/mappings/",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelMappingsPatch(@PathVariable("id") String id, @RequestBody List<Map<String, Object>> patchMappings) {
        return PredictionModelService.patchMappings(id, patchMappings);
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/models/{id}/mappings/} using {@link DeleteMapping}.
     * 
     * Delete all parameter mappings of an {@link AbstractPredictionModel}.
     * I.e., clear the field {@code parameterMappings}.
     * 
     * @param id the {@link AbstractPredictionModel}'s identifier (either its {@code name} or {@code id})
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @DeleteMapping(value = "/{id}/mappings/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modelMappingsDelete(@PathVariable("id") String id) {
        return PredictionModelService.deleteMappings(id);
    }

}
