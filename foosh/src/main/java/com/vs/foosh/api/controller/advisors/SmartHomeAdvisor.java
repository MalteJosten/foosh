package com.vs.foosh.api.controller.advisors;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.vs.foosh.api.exceptions.smarthome.FooSHSmartHomeException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeDeviceFetcherIsNullException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeInstructionExecutorIsNullException;

/**
 * A {@link ControllerAdvice} which intercepts SmartHome-related exceptions and handles them correctly.
 */
@ControllerAdvice
public class SmartHomeAdvisor {
    /**
     * Construct and return a {@link ProblemDetail} if a {@link FooSHSmartHomeException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param exception the thrown {@link FooSHSmartHomeException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @ExceptionHandler({
        SmartHomeAccessException.class,
        SmartHomeIOException.class,
        SmartHomeDeviceFetcherIsNullException.class,
        SmartHomeInstructionExecutorIsNullException.class
    })
    public ResponseEntity<Object> handleSmartHomeAccessException(FooSHSmartHomeException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }
}
