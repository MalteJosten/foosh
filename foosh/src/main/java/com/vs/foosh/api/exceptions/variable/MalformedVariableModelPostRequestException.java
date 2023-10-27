package com.vs.foosh.api.exceptions.variable;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.LinkBuilderService;
import com.vs.foosh.api.services.helpers.ListService;

public class MalformedVariableModelPostRequestException extends FooSHApiException {

    public MalformedVariableModelPostRequestException(String id, String message) {
        super(message, HttpStatus.BAD_REQUEST);
        super.name = "MalformedVariableModelPostRequestException";

        this.links.addAll(LinkBuilderService.getVariableLinkBlock(id));
        this.links.addAll(ListService.getVariableList().getThing(id).getVarModelLinks());
    }

    public MalformedVariableModelPostRequestException(String id, String message, HttpStatus status) {
        super(message, status);
        super.name = "MalformedVariableModelPostRequestException";

        this.links.addAll(LinkBuilderService.getVariableLinkBlock(id));
        this.links.addAll(ListService.getVariableList().getThing(id).getVarModelLinks());
    }
    
}
