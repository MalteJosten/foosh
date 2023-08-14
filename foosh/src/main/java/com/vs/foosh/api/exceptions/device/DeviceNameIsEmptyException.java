package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilder;

public class DeviceNameIsEmptyException extends FooSHApiException {

    public DeviceNameIsEmptyException(UUID uuid, String name) {
        super("The provided value for the field 'name' (" + name + ") is empty!", HttpStatus.BAD_REQUEST);

        this.links.addAll(LinkBuilder.getDeviceLinkWithDevices(uuid.toString()));
    }
}
