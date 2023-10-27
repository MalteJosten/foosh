package com.vs.foosh.api.exceptions.device;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.LinkBuilderService;

public class DeviceNameIsNullException extends FooSHApiException {

    public DeviceNameIsNullException(UUID uuid, Map<String, String> requestBody) {
        super("The provided request body " + requestBody.toString() + " does not contain a field named 'name'!", HttpStatus.BAD_REQUEST);
        super.name = "DeviceNameIsNullException";

        this.links.addAll(LinkBuilderService.getDeviceLinkWithDevices(uuid.toString()));
    }
}
