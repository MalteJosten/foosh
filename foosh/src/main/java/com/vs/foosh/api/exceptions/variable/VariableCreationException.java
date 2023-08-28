package com.vs.foosh.api.exceptions.variable;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.ListService;

public class VariableCreationException extends FooSHApiException {

    public VariableCreationException(String message, HttpStatus status) {
        super(message, status);

        this.links.addAll(ListService.getVariableList().getLinks("self"));
    }
    
}
