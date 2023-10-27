package com.vs.foosh.api.exceptions.variable;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class VariableCreationException extends FooSHApiException {

    public VariableCreationException(String message, HttpStatus status) {
        super(message, status);
        super.name = "VariableCreationException";

        this.links.addAll(ListService.getVariableList().getLinks("self"));
    }
    
}
