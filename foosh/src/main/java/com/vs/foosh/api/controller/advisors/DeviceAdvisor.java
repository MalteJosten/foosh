package com.vs.foosh.api.controller.advisors;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;

@ControllerAdvice
public class DeviceAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
        IdIsNoValidUUIDException.class,
        DeviceNameIsNotUniqueException.class,
        DeviceNameIsNullException.class,
        DeviceNameIsEmptyException.class,
        DeviceIdNotFoundException.class,
        CouldNotFindUniqueDeviceNameException.class})
    public ResponseEntity<Object> handleFooSHApiException(FooSHApiException exception,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

}
