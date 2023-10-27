package com.vs.foosh.api.exceptions.smarthome;

import org.springframework.http.HttpStatus;

public class SmartHomeDeviceFetcherIsNullException extends FooSHSmartHomeException {
    public SmartHomeDeviceFetcherIsNullException() {
        super(
            "There is no registered SmartHomeDeviceFetcher. Please register one before fetching available devices.",
            HttpStatus.INTERNAL_SERVER_ERROR);
        super.name = "SmartHomeDEviceFetcherIsNullException";
    }
}
