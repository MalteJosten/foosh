package com.vs.foosh.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.vs.foosh.api.services.helpers.LinkBuilderService;

@RestController
public class RootController {

    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("/api");
    }

    @GetMapping("/api")
    public ResponseEntity<Object> apiIndex() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "FooSH v0.0.1");
        responseBody.put("_links", LinkBuilderService.getRootLinkEntries());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
