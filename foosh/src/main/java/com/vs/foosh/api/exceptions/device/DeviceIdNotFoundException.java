package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class DeviceIdNotFoundException extends FooSHApiException {

    public DeviceIdNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND);

        this.links.addAll(ListService.getDeviceList().getLinks("devices"));

        try {
            UUID uniqueId = UUID.fromString(id);
            this.message = "Could not find device with id '" + uniqueId + "'!";
        } catch (IllegalArgumentException e) {
            this.message = "Could not find device with name '" + id.toLowerCase() + "'!";
        }
    }

}
