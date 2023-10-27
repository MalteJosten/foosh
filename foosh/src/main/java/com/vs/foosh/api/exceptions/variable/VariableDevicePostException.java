package com.vs.foosh.api.exceptions.variable;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class VariableDevicePostException extends FooSHApiException {
   
    public VariableDevicePostException(String variableId, String message, HttpStatus status) {
        super(message, status);
        super.name = "VariableDevicePostException";

        this.links.addAll(ListService.getVariableList().getThing(variableId).getSelfLinks());
        this.links.addAll(ListService.getVariableList().getThing(variableId).getVarDeviceLinks());
    }

}
