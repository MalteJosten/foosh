package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilder;

public class VariablePredictionException extends FooSHApiException {

    public VariablePredictionException(UUID uuid, String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        
        this.links.addAll(LinkBuilder.getVariableLinkBlock(uuid.toString()));
    }
    
}
