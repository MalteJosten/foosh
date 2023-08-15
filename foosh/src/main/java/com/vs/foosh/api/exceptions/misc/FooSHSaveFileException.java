package com.vs.foosh.api.exceptions.misc;

import org.springframework.http.HttpStatus;

public class FooSHSaveFileException extends RuntimeException {
    private HttpStatus status;

    public FooSHSaveFileException(String message, HttpStatus status) {
        super(message);   
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
    
}
