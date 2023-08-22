package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.ListService;

public class PredictionModelPostMappingException extends FooSHApiException {


    public PredictionModelPostMappingException(String modelId, String message) {
        super(message, HttpStatus.BAD_REQUEST);

        this.links.addAll(ListService.getPredictionModelList().getThing(modelId).getSelfLinks());
    }
    
}
