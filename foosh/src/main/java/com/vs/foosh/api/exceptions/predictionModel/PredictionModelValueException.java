package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class PredictionModelValueException extends FooSHApiException {
    public PredictionModelValueException(String message) {
        super(message, HttpStatus.BAD_REQUEST);

        this.links.addAll(ListService.getPredictionModelList().getLinks("models"));
    }

}
