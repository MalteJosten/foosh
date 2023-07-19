package com.vs.foosh.api.controller;

import java.util.List;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FooshErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            HttpStatus statusCode = HttpStatus.valueOf(Integer.valueOf(status.toString()));

            if (statusCode == HttpStatus.NOT_FOUND) {
                return HttpResponseBuilder.buildException("Page not found!", List.of(LinkBuilder.getRootLinkEntry()), HttpStatus.NOT_FOUND);
            }
        }

        return HttpResponseBuilder.buildException("An error occurred!", List.of(LinkBuilder.getRootLinkEntry()), HttpStatus.I_AM_A_TEAPOT);
    }
}
