package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.ListService;

public class PredictionModelNotFoundException extends FooSHApiException {

    public PredictionModelNotFoundException(String id) {
        super("Could not find prediction model with identifier '" + id + "'!", HttpStatus.BAD_REQUEST);

        this.links.addAll(ListService.getPredictionModelList().getLinks("models"));
    }

}
