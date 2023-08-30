package com.vs.foosh.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vs.foosh.api.services.helpers.LinkBuilderService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FooshErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "An error occurred!";
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (status != null) {
            HttpStatus statusCode = HttpStatus.valueOf(Integer.valueOf(status.toString()));

            if (statusCode == HttpStatus.NOT_FOUND) {
                httpStatus = HttpStatus.NOT_FOUND;
                message = "Page not found!";
            }
        }


        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("_links", LinkBuilderService.getRootLinkEntries());

        return new ResponseEntity<>(responseBody, httpStatus);
    }
}
