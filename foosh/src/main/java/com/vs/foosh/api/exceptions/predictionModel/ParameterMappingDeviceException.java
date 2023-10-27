package com.vs.foosh.api.exceptions.predictionModel;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.LinkBuilderService;
import com.vs.foosh.api.services.helpers.ListService;

public class ParameterMappingDeviceException extends FooSHApiException {

    public ParameterMappingDeviceException(String modelId, UUID variableUuid, UUID deviceUuid) {
        super("The device '" + ListService.getDeviceList().getThing(deviceUuid.toString()).getDeviceName() + "' (" + deviceUuid +
                ") is not assigned to the variable '" + ListService.getVariableList().getThing(variableUuid.toString()).getName() + "' (" + variableUuid + ")!", HttpStatus.BAD_REQUEST);
        super.name = "ParameterMappingDeviceException";
        
        this.links.addAll(LinkBuilderService.getPredictionModelLinkBlock(modelId));
        this.links.addAll(ListService.getVariableList().getThing(variableUuid.toString()).getSelfStaticLinks("variable"));
        this.links.addAll(ListService.getDeviceList().getThing(deviceUuid.toString()).getSelfStaticLinks("device"));
    }

}
