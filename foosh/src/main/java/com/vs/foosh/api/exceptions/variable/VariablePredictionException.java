package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.LinkBuilderService;

public class VariablePredictionException extends FooSHApiException {

    public VariablePredictionException(UUID uuid, String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        super.name = "VariablePredictionException";
        
        this.links.addAll(LinkBuilderService.getVariableLinkBlock(uuid.toString()));
    }
    
}
