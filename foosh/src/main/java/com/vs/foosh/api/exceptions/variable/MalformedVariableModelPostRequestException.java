package com.vs.foosh.api.exceptions.variable;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

public class MalformedVariableModelPostRequestException extends FooSHApiException {

    public MalformedVariableModelPostRequestException(String id, String message) {
        super(message, HttpStatus.BAD_REQUEST);

        this.links.addAll(LinkBuilder.getVariableLinkBlock(id));
        this.links.addAll(ListService.getVariableList().getThing(id).getVarModelLinks());
    }
    
}
