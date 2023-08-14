package com.vs.foosh.api.exceptions.misc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.model.web.LinkEntry;

public class FooSHApiException extends RuntimeException {
    protected HttpStatus status;
    protected List<LinkEntry> links = new ArrayList<>();
    protected String message;

    public FooSHApiException(HttpStatus status) {
        super();
        this.status  = status;
        this.message = "";
    }

    public FooSHApiException(String message, HttpStatus status) {
        super();
        this.message = message;
        this.status  = status;
    }

    protected void setMessage(String message) {
       this.message = message; 
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public List<LinkEntry> getLinks() {
        return this.links;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
