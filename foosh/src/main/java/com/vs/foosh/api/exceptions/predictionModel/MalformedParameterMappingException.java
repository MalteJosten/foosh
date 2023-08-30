package com.vs.foosh.api.exceptions.predictionModel;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.helpers.LinkBuilderService;

public class MalformedParameterMappingException extends FooSHApiException {

    public MalformedParameterMappingException(String id, String message) {
        super(message, HttpStatus.BAD_REQUEST);

        this.links.addAll(LinkBuilderService.getPredictionModelLinkBlock(id));
        this.links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.GET, List.of()));
    }
}
