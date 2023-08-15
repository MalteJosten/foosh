package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.ListService;

public class PredictionModelNameMustNotBeAnUuidException extends FooSHApiException {

    public PredictionModelNameMustNotBeAnUuidException() {
        super("The name must not be an UUID!", HttpStatus.BAD_REQUEST);

        this.links.addAll(ListService.getPredictionModelList().getLinks("models"));
    }
    
}
