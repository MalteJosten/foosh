package com.vs.foosh.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.vs.foosh.api.services.helpers.LinkBuilderService;

/**
 * A {@link RestController} that handles HTTP requests for the routes {@code /} and {@code /api}.
 */
@RestController
public class RootController {

    /**
     * Handle incoming {@code GET} requests on route {@code /} using {@link GetMapping}.
     * 
     * Redirect to {@code /api}.
     * 
     * @return a {@link RedirectView} with the redirection URL "{@code /api}"
     */
    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("/api");
    }

    /**
     * Handle incoming {@code GET} requests on route {@code /api} using {@link GetMapping}.
     * 
     * Response with the appliction's version and the list of available "root" routes.
     * @see {@link com.vs.foosh.api.services.helpers.LinkBuilderService#getRootLinkEntries() getRootLinkEntries()}
     * 
     * @return a {@link ResponseEntity} containing the version and list available routes
     */
    @GetMapping("/api")
    public ResponseEntity<Object> apiIndex() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "FooSH v0.0.1");
        responseBody.put("_links", LinkBuilderService.getRootLinkEntries());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
