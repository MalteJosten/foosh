package com.vs.foosh.api.services;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.LinkEntry;

public class HttpResponseBuilder {
    public static ResponseEntity<Object> buildResponse(Thing object, List<LinkEntry> links, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(object.getClass().getSimpleName().toLowerCase(), object);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildResponse(List<Thing> collection, String collectionLabel, List<LinkEntry> links, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(collectionLabel.toLowerCase(), collection);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildResponse(AbstractMap.Entry<String, Object> result, List<LinkEntry> links, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildException(String message, List<LinkEntry> links, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildException(String message, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);

        return new ResponseEntity<>(responseBody, status);
    }
}
