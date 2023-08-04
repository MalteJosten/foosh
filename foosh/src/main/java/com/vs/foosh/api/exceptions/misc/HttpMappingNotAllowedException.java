package com.vs.foosh.api.exceptions.misc;

import java.util.ArrayList;
import java.util.List;

import com.vs.foosh.api.model.web.LinkEntry;

public class HttpMappingNotAllowedException extends RuntimeException {
    private String message;
    private List<LinkEntry> returnPaths = new ArrayList<>();

    public HttpMappingNotAllowedException(String message) {
        this.message = message;
    }

    public HttpMappingNotAllowedException(String message, List<LinkEntry> returnPaths) {
        this.message     = message;
        this.returnPaths = returnPaths;
    }

    public String getMessage()  {
        return this.message;
    }
    
    public List<LinkEntry> getReturnPaths() {
        return this.returnPaths;
    }
}
