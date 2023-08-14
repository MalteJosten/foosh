package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.LinkBuilder;

public class CouldNotFindUniqueDeviceNameException extends FooSHApiException {

    public CouldNotFindUniqueDeviceNameException(UUID uuid, int timeoutCount) {
        super("Could not find an unique name for device " + uuid + " after " + timeoutCount + " tries.", HttpStatus.BAD_REQUEST);

        this.links = LinkBuilder.getDeviceLinkWithDevices(uuid.toString());
    }

}
