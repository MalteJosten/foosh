package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class PredictionModelNameIsNotUniqueException extends FooSHApiException {

    public PredictionModelNameIsNotUniqueException(String name) {
        super("The name '" + name + "' is already used!", HttpStatus.BAD_REQUEST);
        super.name = "PredictionModelNameIsnotUniqueException";

        this.links.addAll(ListService.getPredictionModelList().getLinks("models"));
    }

}