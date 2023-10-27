package com.vs.foosh.api.exceptions.smarthome;

import org.springframework.http.HttpStatus;

public class SmartHomeInstructionExecutorIsNullException extends FooSHSmartHomeException {
    public SmartHomeInstructionExecutorIsNullException() {
        super(
            "There is no registered SmartHomeInstructionExecutor. Please register one before fetching available devices.",
            HttpStatus.INTERNAL_SERVER_ERROR);
        super.name = "SmartHomeInstructionExecutionIsNullException";
    }
    
}
