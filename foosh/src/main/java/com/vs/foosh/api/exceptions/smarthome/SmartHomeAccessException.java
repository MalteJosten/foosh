package com.vs.foosh.api.exceptions.smarthome;

public class SmartHomeAccessException extends RuntimeException {
    private String uri;

    public SmartHomeAccessException(String uri) {
        super("Could not access Smart Home API at '" + uri + "'!");
        this.uri = uri;
    }
    
    public String getUri() { return this.uri; }
}
