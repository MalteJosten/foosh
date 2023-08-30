package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class ParameterMappingAlreadyPresentException extends FooSHApiException {

    public ParameterMappingAlreadyPresentException(String message) {
        super(message, HttpStatus.CONFLICT);
        
        this.links.addAll(ListService.getPredictionModelList().getLinks("models"));
    }
    
}
