package com.vs.foosh.api.exceptions;

import java.net.URI;
import java.util.Map;

public class HttpMappingNotAllowedException extends RuntimeException {
    private String message;
    private Map<String, URI> returnPath;

    public HttpMappingNotAllowedException(String message) {
        this.message = message;
    }

    public HttpMappingNotAllowedException(String message, Map<String, URI> returnPath) {
        this.message    = message;
        this.returnPath = returnPath;
    }

    public String getMessage()  {
        return this.message;
    }
    
    public Map<String, URI> getReturnPath()  {
        return this.returnPath;
    }
}
