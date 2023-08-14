package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilderService;

public class CouldNotMakePredictionException extends FooSHApiException {

    public CouldNotMakePredictionException(UUID variableUuid, String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);

        this.links.addAll(LinkBuilderService.getVariableLinkBlock(variableUuid.toString()));
    }

}
