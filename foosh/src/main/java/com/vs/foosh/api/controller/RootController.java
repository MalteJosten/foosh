package com.vs.foosh.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RootController {

    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("/api");
    }

    @GetMapping("/api")
    public String apiIndex() {
        return "FooSH v0.0.1";
    }
}
