package com.vs.foosh.api.exceptions.smarthome;

public class SmartHomeIOException extends RuntimeException {
    private String uri;

    public SmartHomeIOException(String uri) {
        super("A timeout occurred while tryping to retrieve device list from Smart Home API at '" + uri + "'!");
        this.uri = uri;
    }

    public String getUri() { return this.uri; }
    
}
