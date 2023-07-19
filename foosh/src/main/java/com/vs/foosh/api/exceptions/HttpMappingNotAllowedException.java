package com.vs.foosh.api.exceptions;

import com.vs.foosh.api.model.LinkEntry;

public class HttpMappingNotAllowedException extends RuntimeException {
    private String message;
    private LinkEntry returnPath;

    public HttpMappingNotAllowedException(String message) {
        this.message = message;
    }

    public HttpMappingNotAllowedException(String message, LinkEntry returnPath) {
        this.message    = message;
        this.returnPath = returnPath;
    }

    public String getMessage()  {
        return this.message;
    }
    
    public LinkEntry getReturnPath()  {
        return this.returnPath;
    }
}
