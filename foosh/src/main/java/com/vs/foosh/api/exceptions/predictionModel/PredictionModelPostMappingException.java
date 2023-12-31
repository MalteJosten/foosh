package com.vs.foosh.api.exceptions.predictionModel;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class PredictionModelPostMappingException extends FooSHApiException {


    public PredictionModelPostMappingException(String modelId, String message) {
        super(message, HttpStatus.BAD_REQUEST);
        super.name = "PredictionmodelPostMappingException";

        this.links.addAll(ListService.getPredictionModelList().getThing(modelId).getSelfLinks());
    }
    
}
