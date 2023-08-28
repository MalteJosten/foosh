package com.vs.foosh.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableDevicesPostRequest;
import com.vs.foosh.api.model.variable.VariableModelPostRequest;
import com.vs.foosh.api.model.variable.VariablePostRequest;
import com.vs.foosh.api.model.variable.VariablePredictionRequest;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.VariableService;

/**
 * A {@link RestController} that handles HTTP requests for the routes {@code /api/vars/}, {@code /api/vars/{id}}, {@code /api/vars/{id}/devices/}, and {@code /api/vars/{id}/models/}.
 */
@RestController
@RequestMapping(value = "/api/vars")
public class VariableController {

    private final String ROUTE = "/api/vars/";

    ///
    /// Variable Collection
    ///

    /**
     * Handle incoming {@code GET} requests on route {@code /api/vars/} using {@link GetMapping}.
     * 
     * Retrieve the list of {@link Variable}s.
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varsGet() {
        return VariableService.getVariables();
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/vars/} using {@link PostMapping}.
     * 
     * One can post multiple {@link Variable}s at once.
     * 
     * @apiNote It only accepts {@code application/json} {@link MediaType}s.
     *          It is called with the header field {@code batch} set to {@code true}.
     * 
     * @param requests the list with elements of type {@link VariablePostRequest}
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PostMapping(value = "/",
            headers  = "batch=true",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varsPostMultiple(@RequestBody List<VariablePostRequest> requests) {
        return VariableService.addVariables(requests);
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/vars/} using {@link PostMapping}.
     * 
     * One can post a single {@link Variable}.
     * 
     * @apiNote It only accepts {@code application/json} {@link MediaType}s.
     *          It is called with the header field {@code batch} set to {@code false}.
     * 
     * @param request the {@link VariablePostRequest}
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PostMapping(value = "/",
            headers  = "batch=false",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varsPostSingle(@RequestBody VariablePostRequest request) {
        return VariableService.addVariable(request);
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/vars/} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void varsPut() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + "! Use DELETE and POST to replace the list of variables.",
                ListService.getVariableList().getLinks("self"));
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/vars/} using {@link PatchMapping}.
     * 
     * @apiNote Using {@code PATCH} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PatchMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void varsPatch() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on " + ROUTE + "! Either use PATCH on /vars/{id} to update a variable's name or DELETE and POST on /vars/ to replace the list of variables.",
                ListService.getVariableList().getLinks("self"));
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/vars} using {@link DeleteMapping}.
     * 
     * Delete the list of {@link Variable}s.
     *
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varsDelete() {
        return VariableService.deleteVariables();
    }

    ///
    /// Variable
    ///

    /**
     * Handle incoming {@code GET} requests on route {@code /api/vars/{id}} using {@link GetMapping}.
     * 
     * Get a specific {@link Variable}.
     * 
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @return the HTTP repsonse as a {@link ResponseEntity}
     */
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varGet(@PathVariable("id") String id) {
        return VariableService.getVariable(id);
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/vars/{id}} using {@link PostMapping}.
     * 
     * Retrieve (and execute) smart home API instructions, given a desired value of a {@link Variable}.
     * 
     * @apiNote It only accepts {@code application/json} {@link MediaType}s.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @param request the {@link VariablePredictionRequest}
     * 
     * @return the HTTP repsonse as a {@link ResponseEntity}
     */
    @PostMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varPost(@PathVariable("id") String id, @RequestBody VariablePredictionRequest request) {
        return VariableService.startVariablePrediction(id, request);
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/vars/{id}} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     */
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void varPut(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + id.replace(" ", "%20") +  "! Either use PATCH to update or DELETE and POST to replace a variable.",
                ListService.getVariableList().getThing(id).getSelfLinks());
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/vars/{id}} using {@link PatchMapping}.
     * 
     * Patch (Edit) the name of a specific {@link Variable}.
     * 
     * @apiNote It only accepts {@code application/json-patch+json} {@link MediaType}s.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc6902">RFC 6902: JavaScript Object Notation Patch</a>
     * @apiNote {id} in the route can only be the {@link Variable}'s {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @param request the {@link VariablePredictionRequest}
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PatchMapping(value = "/{id}",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varPatch(@PathVariable("id") UUID uuid, @RequestBody List<Map<String, Object>> patchMappings) {
        return VariableService.patchVariable(uuid.toString(), patchMappings);
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/vars/{id}} using {@link DeleteMapping}.
     * 
     * Delete a specific {@link Variable}.
     * 
     * @apiNote Using {@code DELETE} on this route is not allowed. Hence, a {@link HttpmAppingNotAllowedException} is thrown.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     */
    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varDelete(@PathVariable("id") String id) {
        return VariableService.deleteVariable(id);
    }


    ///
    /// Devices
    ///
    
    /**
     * Handle incoming {@code GET} requests on route {@code /api/vars/{id}/devices/} using {@link GetMapping}.
     * 
     * Retrieve the list of {@link AbstractDevice}s linked to a specific {@link Variable}.
     * 
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @return the HTTP repsonse as a {@link ResponseEntity}
     */
    @GetMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varDevicesGet(@PathVariable("id") String id) {
        return VariableService.getVariableDevices(id);
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/vars/{id}/devices/} using {@link PostMapping}.
     * 
     * Set and link a list of {@link AbstractDevice}s to a specific {@link Variable}.
     * 
     * @apiNote It only accepts {@code application/json} {@link MediaType}s.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @param request the {@link VariableDevicesPostRequest}
     * 
     * @return the HTTP repsonse as a {@link ResponseEntity}
     */
    @PostMapping(value = "/{id}/devices/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varDevicesPost(@PathVariable("id") String id, @RequestBody VariableDevicesPostRequest request) {
        return VariableService.postVariableDevices(id, request);
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/vars/{id}/devices/} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     */
    @PutMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void varDevicesPut(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getDeviceLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + id.replace(" ", "%20") +  "/devices/! Either use PATCH to update or DELETE and POST to replace the list of associated devices.",
                links);
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/vars/{id}/devices/} using {@link PatchMapping}.
     * 
     * Patch (Edit) the list of linked {@link AbstractDevice}s for a specific {@link Variable}.
     * 
     * @apiNote It only accepts {@code application/json-patch+json} {@link MediaType}s.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc6902">RFC 6902: JavaScript Object Notation Patch</a>
     * @apiNote {id} in the route can only be the {@link Variable}'s {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @param patchMappings a Json Patch Document
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PatchMapping(value = "/{id}/devices/",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varDevicesPatch(@PathVariable("id") String id, @RequestBody List<Map<String, Object>> patchMappings) {
        return VariableService.patchVariableDevices(id, patchMappings);
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/vars/{id}/devices/} using {@link DeleteMapping}.
     * 
     * Delete the list of {@link AbstractDevice}s linked to a specific {@link Variable}.
     * 
     * @apiNote Using {@code DELETE} on this route is not allowed. Hence, a {@link HttpmAppingNotAllowedException} is thrown.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     */
    @DeleteMapping(value = "/{id}/devices/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varDevicesDelete(@PathVariable("id") String id) {
        return VariableService.deleteVariableDevices(id);
    }

    ///
    /// Models
    ///

    /**
     * Handle incoming {@code GET} requests on route {@code /api/vars/{id}/models/} using {@link GetMapping}.
     * 
     * Retrieve the list of {@link AbstractPredictionModel}s associated to a specific {@link Variable}.
     * 
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @return the HTTP repsonse as a {@link ResponseEntity}
     */
    @GetMapping(value = "/{id}/models/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varModelsGet(@PathVariable("id") String id) {
        return VariableService.getVariableModels(id);
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/vars/{id}/models/} using {@link PostMapping}.
     * 
     * Associate a specific {@link Variable} to an {@link AbstractPredictionModel}.
     * 
     * @apiNote It only accepts {@code application/json} {@link MediaType}s.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @param request the {@link VariableModelPostRequest}
     * 
     * @return the HTTP repsonse as a {@link ResponseEntity}
     */
    @PostMapping(value = "/{id}/models/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varModelsPost(@PathVariable("id") String id, @RequestBody VariableModelPostRequest request) {
        return VariableService.addVariableModel(id, request);
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/vars/{id}/models/} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     */
    @PutMapping(value = "/{id}/models/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varModelsPut(@PathVariable("id") String id) {
        Variable variable = ListService.getVariableList().getThing(id);
        
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getModelLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE  + id + "/models/! Either use PATCH or DELETE and POST instead.",
                links);
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/vars/{id}/models/} using {@link PatchMapping}.
     * 
     * Patch (Edit) the link between a specific {@link Variable} and its associated {@link AbstractPredictionModel}s.
     * 
     * @apiNote It only accepts {@code application/json-patch+json} {@link MediaType}s.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc6902">RFC 6902: JavaScript Object Notation Patch</a>
     * @apiNote {id} in the route can only be the {@link Variable}'s {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @param patchMappings a Json Patch Document
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PatchMapping(value = "/{id}/models/",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varModelsPatch(@PathVariable("id") String id, @RequestBody List<Map<String, Object>> patchMappings) {
        return VariableService.patchVariableModels(id, patchMappings);
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/vars/{id}/models/} using {@link DeleteMapping}.
     * 
     * Delete the list of associated {@link AbstractPredictionModel}s for a specific {@link Variable}.
     * 
     * @apiNote Using {@code DELETE} on this route is not allowed. Hence, a {@link HttpmAppingNotAllowedException} is thrown.
     * @apiNote {id} in the route can be either the {@link Variable}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     */
    @DeleteMapping(value = "/{id}/models/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> varModelsDelete(@PathVariable("id") String id) {
        return VariableService.deleteVariableModels(id);
    }
}
