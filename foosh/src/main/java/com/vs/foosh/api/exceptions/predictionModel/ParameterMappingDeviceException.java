package com.vs.foosh.api.exceptions.predictionModel;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

public class ParameterMappingDeviceException extends FooSHApiException {

    public ParameterMappingDeviceException(String modelId, UUID variableUuid, UUID deviceUuid) {
        super("The device '" + ListService.getDeviceList().getThing(deviceUuid.toString()).getDeviceName() + "' (" + deviceUuid +
                ") is not assigned to the variable '" + ListService.getVariableList().getThing(variableUuid.toString()).getName() + "' (" + variableUuid + ")!", HttpStatus.BAD_REQUEST);

        this.links.addAll(LinkBuilder.getPredictionModelLinkBlock(modelId));
        this.links.addAll(ListService.getVariableList().getThing(variableUuid.toString()).getSelfStaticLinks("variable"));
        this.links.addAll(ListService.getDeviceList().getThing(deviceUuid.toString()).getSelfStaticLinks("device"));
    }

}
