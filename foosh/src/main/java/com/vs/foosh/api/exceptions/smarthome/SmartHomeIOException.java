package com.vs.foosh.api.exceptions.smarthome;

import org.springframework.http.HttpStatus;

public class SmartHomeIOException extends FooSHSmartHomeException {

    public SmartHomeIOException(String uri) {
        super(
            "A timeout occurred while tryping to retrieve device list from Smart Home API at '" + uri + "'!",
            HttpStatus.GATEWAY_TIMEOUT);
        super.name = "SmartHomeIOException";
    }
    
}
