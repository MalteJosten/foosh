package com.vs.foosh.api.services;

import java.net.URI;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vs.foosh.api.model.AbstractDevice;
import com.vs.foosh.api.model.LinkEntry;

public class HttpResponseBuilder {
    public static ResponseEntity<Object> buildResponse(AbstractDevice device, Map<String, URI> linkBlock, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("device", device);
        responseBody.put("links", linkBlock);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildResponse(AbstractDevice device, List<LinkEntry> links, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("device", device);
        responseBody.put("links", links);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildResponse(AbstractMap.Entry<String, Object> result, List<LinkEntry> links, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("links", links);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildException(String message, Map<String, URI> linkBlock, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("links", linkBlock);

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> buildException(String message, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);

        return new ResponseEntity<>(responseBody, status);
    }
}
