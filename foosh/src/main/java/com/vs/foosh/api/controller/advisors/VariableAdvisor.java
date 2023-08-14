package com.vs.foosh.api.controller.advisors;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.exceptions.variable.CouldNotMakePredictionException;
import com.vs.foosh.api.exceptions.variable.MalformedVariableModelPostRequestException;
import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.exceptions.variable.VariablePredictionException;

@ControllerAdvice
public class VariableAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
        VariableNotFoundException.class,
        VariableCreationException.class,
        CouldNotMakePredictionException.class,
        VariableNameIsNotUniqueException.class,
        VariableNameMustNotBeAnUuidException.class,
        VariableDevicePostException.class,
        VariablePredictionException.class,
        MalformedVariableModelPostRequestException.class,
        MalformedVariablePredictionRequest.class
    })
    public ResponseEntity<Object> handleFooSHApiExceptions(FooSHApiException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

}
