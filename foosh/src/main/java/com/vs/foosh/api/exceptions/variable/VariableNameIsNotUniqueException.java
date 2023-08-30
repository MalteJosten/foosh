package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.LinkBuilderService;
import com.vs.foosh.api.services.helpers.ListService;

public class VariableNameIsNotUniqueException extends FooSHApiException {

    public VariableNameIsNotUniqueException(UUID variableUuid, String name) {
        super("The name '" + name + "' is already used!", HttpStatus.CONFLICT);
        
        if (variableUuid == null) {
            this.links.addAll(ListService.getVariableList().getLinks("variables"));
        } else {
            this.links.addAll(LinkBuilderService.getVariableLinkBlock(variableUuid.toString()));
        }
    }
}
