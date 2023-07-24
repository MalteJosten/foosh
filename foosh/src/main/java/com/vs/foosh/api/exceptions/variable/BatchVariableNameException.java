package com.vs.foosh.api.exceptions.variable;

public class BatchVariableNameException extends RuntimeException {
    public BatchVariableNameException() {
        super("Could not execute batch name update!");
    }
}
