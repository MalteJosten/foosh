package com.vs.foosh.api.exceptions.misc;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.model.misc.ThingType;
import com.vs.foosh.api.services.ListService;

public class IdIsNoValidUUIDException extends FooSHApiException {

    public IdIsNoValidUUIDException(String id, ThingType type) {
        super("The provided id '" + id + "' is not a valid UUID!", HttpStatus.BAD_REQUEST);
    
        switch (type) {
            case DEVICE:
                this.links = ListService.getDeviceList().getLinks("devices");
                break;
            case VARIABLE:
                this.links = ListService.getVariableList().getLinks("variables");
                break;
            case PREDICTION_MODEL:
                this.links = ListService.getPredictionModelList().getLinks("predictionModel");
                break;
        }
    }
}
