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
import com.vs.foosh.api.exceptions.predictionModel.CouldNotFindVariableParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingAlreadyPresentException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingDeviceException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingNotFoundException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNotFoundException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelPostMappingException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelValueException;
import com.vs.foosh.api.exceptions.variable.CouldNotMakePredictionException;
import com.vs.foosh.api.exceptions.variable.MalformedVariableModelPostRequestException;
import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.exceptions.variable.VariablePredictionException;

/**
 * A {@link ControllerAdvice} which intercepts {@link Thing}-related exceptions and handles them correctly.
 */
@ControllerAdvice
public class ThingAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Construct and return a {@link ProblemDetail} if a {@link AbstractDevice}-related {@link FooSHApiException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param exception the thrown {@link FooSHApiException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @ExceptionHandler({
        IdIsNoValidUUIDException.class,
        DeviceNameIsNotUniqueException.class,
        DeviceNameIsNullException.class,
        DeviceNameIsEmptyException.class,
        DeviceIdNotFoundException.class,
        CouldNotFindUniqueDeviceNameException.class
    })
    public ResponseEntity<Object> handleDeviceException(FooSHApiException exception,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setTitle(exception.getName());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

    /**
     * Construct and return a {@link ProblemDetail} if a {@link AbstractPredictionModel}-related {@link FooSHApiException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param exception the thrown {@link FooSHApiException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @ExceptionHandler({
        MalformedParameterMappingException.class,
        CouldNotFindVariableParameterMappingException.class,
        ParameterMappingDeviceException.class,
        ParameterMappingAlreadyPresentException.class,
        ParameterMappingNotFoundException.class,
        PredictionModelNotFoundException.class,
        PredictionModelValueException.class,
        PredictionModelNameMustNotBeAnUuidException.class,
        PredictionModelNameIsNotUniqueException.class,
        PredictionModelPostMappingException.class
    })
    public ResponseEntity<Object> handlePredictionModelException(FooSHApiException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setTitle(exception.getName());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

    /**
     * Construct and return a {@link ProblemDetail} if a {@link Variable}-related {@link FooSHApiException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param exception the thrown {@link FooSHApiException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
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
    public ResponseEntity<Object> handleVariableExceptions(FooSHApiException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setTitle(exception.getName());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

}
