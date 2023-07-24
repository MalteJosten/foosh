package com.vs.foosh.api.exceptions.device;

public class BatchDeviceNameException extends RuntimeException {
    public BatchDeviceNameException() {
        super("Could not execute batch name update!");
    }
}
