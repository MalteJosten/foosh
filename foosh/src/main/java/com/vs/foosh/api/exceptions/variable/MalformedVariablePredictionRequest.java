package com.vs.foosh.api.exceptions.variable;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.LinkBuilderService;

public class MalformedVariablePredictionRequest extends FooSHApiException {

    public MalformedVariablePredictionRequest(String id, String message) {
        super(message, HttpStatus.BAD_REQUEST);
        super.name = "MalformedVariablePredictionRequest";

        this.links.addAll(LinkBuilderService.getVariableLinkBlock(id));
    }

}
