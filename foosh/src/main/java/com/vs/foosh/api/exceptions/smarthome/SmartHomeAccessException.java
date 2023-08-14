package com.vs.foosh.api.exceptions.smarthome;

import org.springframework.http.HttpStatus;

public class SmartHomeAccessException extends FooSHSmartHomeException {

    public SmartHomeAccessException(String uri) {
        super("Could not access Smart Home API at '" + uri + "'!", HttpStatus.BAD_GATEWAY);
    }
}
