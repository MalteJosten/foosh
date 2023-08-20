package com.vs.foosh.api.controller.advisors;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.exceptions.predictionModel.CouldNotFindVariableParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingAlreadyPresentException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingDeviceException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingNotFoundException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNotFoundException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelValueException;

@ControllerAdvice
public class PredictionModelAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
        MalformedParameterMappingException.class,
        CouldNotFindVariableParameterMappingException.class,
        ParameterMappingDeviceException.class,
        ParameterMappingAlreadyPresentException.class,
        ParameterMappingNotFoundException.class,
        PredictionModelNotFoundException.class,
        PredictionModelValueException.class,
        PredictionModelNameMustNotBeAnUuidException.class,
        PredictionModelNameIsNotUniqueException.class
    })
    public ResponseEntity<Object> handleFooSHApiException(FooSHApiException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

}
