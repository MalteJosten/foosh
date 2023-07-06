package com.vs.foosh.api.model.exceptions;

public class SmartHomeAccessException extends RuntimeException {
    private String uri;

    public SmartHomeAccessException(String uri) {
        super();
        this.uri = uri;
    }
    
    public String getUri() { return this.uri; }
}
