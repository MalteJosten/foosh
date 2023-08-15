package com.vs.foosh.api.exceptions.predictionModel;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilderService;

public class CouldNotFindVariableParameterMappingException extends FooSHApiException {
    
    public CouldNotFindVariableParameterMappingException(UUID uuid, UUID variableUuid) {
        super("Could not find parameter mapping for variable " + variableUuid + "!", HttpStatus.BAD_REQUEST);

        this.links.addAll(LinkBuilderService.getPredictionModelLinkBlock(uuid.toString()));
        this.links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.GET, List.of()));
    }
}
