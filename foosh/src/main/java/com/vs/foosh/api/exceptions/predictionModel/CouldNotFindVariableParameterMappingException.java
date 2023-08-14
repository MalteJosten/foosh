package com.vs.foosh.api.exceptions.predictionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;

public class CouldNotFindVariableParameterMappingException extends FooSHApiException {
    
    public CouldNotFindVariableParameterMappingException(UUID uuid, UUID variableUuid) {
        super("Could not find parameter mapping for variable " + variableUuid + "!", HttpStatus.BAD_REQUEST);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(LinkBuilder.getPredictionModelLinkBlock(uuid.toString()));
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.GET, List.of()));
    }
}
