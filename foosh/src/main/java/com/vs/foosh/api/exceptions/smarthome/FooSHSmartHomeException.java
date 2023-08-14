package com.vs.foosh.api.exceptions.smarthome;

import org.springframework.http.HttpStatus;

public class FooSHSmartHomeException extends RuntimeException {
    private HttpStatus status;

    public FooSHSmartHomeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
    
}
