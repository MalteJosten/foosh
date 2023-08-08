package com.vs.foosh.api.exceptions.smarthome;

public class SmartHomeDeviceFetcherIsNullException extends RuntimeException {
    public SmartHomeDeviceFetcherIsNullException() {
        super("There is no registered SmartHomeDeviceFetcher. Please register one before fetching available devices.");
    }
}
