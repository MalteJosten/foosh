package com.vs.foosh.api.exceptions.misc;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.model.misc.ThingType;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.ListService;

public class IdIsNoValidUUIDException extends RuntimeException {
    private HttpStatus status;
    private List<LinkEntry> links;
    
    public IdIsNoValidUUIDException(String id, ThingType type) {
        super("The provided id '" + id + "' is not a valid UUID!");
        this.status = HttpStatus.BAD_REQUEST;
    
        switch (type) {
            case DEVICE:
                links = ListService.getDeviceList().getLinks("devices");
                break;
            case VARIABLE:
                links = ListService.getVariableList().getLinks("variables");
                break;
            case MODEL:
                links = ListService.getPredictionModelList().getLinks("predictionModel");
                break;
        }
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public List<LinkEntry> getLinks() {
        return this.links;
    }
}
