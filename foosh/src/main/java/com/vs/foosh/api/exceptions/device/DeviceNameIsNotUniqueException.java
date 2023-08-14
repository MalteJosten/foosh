package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilder;

public class DeviceNameIsNotUniqueException extends FooSHApiException {

    public DeviceNameIsNotUniqueException(UUID uuid, String name) {
        super("The name '" + name + "' is already used!", HttpStatus.CONFLICT);

        this.links.addAll(LinkBuilder.getDeviceLinkWithDevices(uuid.toString()));
    }

}
