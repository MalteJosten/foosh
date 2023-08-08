package com.vs.foosh.api.exceptions.smarthome;

public class SmartHomeInstructionExecutorIsNullException extends RuntimeException {
    public SmartHomeInstructionExecutorIsNullException() {
        super("There is no registered SmartHomeInstructionExecutor. Please register one before fetching available devices.");
    }
    
}
