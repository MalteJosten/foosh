package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilder;

public class VariableNameMustNotBeAnUuidException extends FooSHApiException {

    public VariableNameMustNotBeAnUuidException(UUID variableUuid) {
        super("The name must not be an UUID!", HttpStatus.BAD_REQUEST);
        
        this.links = LinkBuilder.getVariableLinkBlock(variableUuid.toString());
    }

}
