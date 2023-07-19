package com.vs.foosh.api.exceptions.device;

public class BatchQueryNameException extends RuntimeException {
    public BatchQueryNameException() {
        super("Could not execute batch query name update!");
    }
}
