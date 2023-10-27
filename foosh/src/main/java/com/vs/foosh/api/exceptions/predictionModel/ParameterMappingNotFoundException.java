package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class ParameterMappingNotFoundException extends FooSHApiException {

    public ParameterMappingNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
        super.name = "ParameterMappingnotFoundException";
        
        this.links.addAll(ListService.getPredictionModelList().getLinks("models"));
    }
    
}
