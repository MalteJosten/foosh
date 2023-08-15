package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.ListService;

public class PredictionModelNameIsNotUniqueException extends FooSHApiException {

    public PredictionModelNameIsNotUniqueException(String name) {
        super("The name '" + name + "' is already used!", HttpStatus.BAD_REQUEST);

        this.links.addAll(ListService.getPredictionModelList().getLinks("models"));
    }

}