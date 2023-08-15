package com.vs.foosh.api.exceptions.misc;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.model.web.LinkEntry;

public class HttpMappingNotAllowedException extends FooSHApiException {

    public HttpMappingNotAllowedException(String message, List<LinkEntry> returnPaths) {
        super(message, HttpStatus.METHOD_NOT_ALLOWED);

        this.links.addAll(returnPaths);
    }

}
