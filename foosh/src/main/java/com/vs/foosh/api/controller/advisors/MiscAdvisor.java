package com.vs.foosh.api.controller.advisors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsNullException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueException;
import com.vs.foosh.api.exceptions.misc.CouldNotDeleteCollectionException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.misc.SavingToFileIOException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeDeviceFetcherIsNullException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeInstructionExecutorIsNullException;
import com.vs.foosh.api.services.HttpResponseBuilder;

@ControllerAdvice
public class MiscAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SmartHomeAccessException.class)
    public ResponseEntity<Object> handleSmartHomeAccessException(SmartHomeAccessException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(SmartHomeIOException.class)
    public ResponseEntity<Object> handleSmartHomeIOException(SmartHomeIOException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(SmartHomeDeviceFetcherIsNullException.class)
    public ResponseEntity<Object> handleSmartHomeDeviceFetcherIsNullException(SmartHomeDeviceFetcherIsNullException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SmartHomeInstructionExecutorIsNullException.class)
    public ResponseEntity<Object> handleSmartHomeExecutorIsNullException(SmartHomeInstructionExecutorIsNullException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMappingNotAllowedException.class)
    public ResponseEntity<Object> handleHttpMappingNotAllowedExeption(HttpMappingNotAllowedException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                exception.getReturnPaths(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(SaveFileNotFoundException.class)
    public ResponseEntity<Object> handleSaveFileNotFoundException(SaveFileNotFoundException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SavingToFileIOException.class)
    public ResponseEntity<Object> handleSavingToFileIOException(SavingToFileIOException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CouldNotDeleteCollectionException.class)
    public ResponseEntity<Object> handleCouldNotDeleteCollection(CouldNotDeleteCollectionException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {
        FooSHJsonPatchFormatException.class,
        FooSHJsonPatchIllegalArgumentException.class,
        FooSHJsonPatchIllegalOperationException.class,
        FooSHJsonPatchValueException.class,
        FooSHJsonPatchValueIsEmptyException.class,
        FooSHJsonPatchValueIsNullException.class})
    public ResponseEntity<Object> handleFooSHJsonPatchExceptions(RuntimeException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    ///
    /// Spring Boot Overrides
    ///

    // TODO: Test each API Endpoint and make sure to cover all @RequestBody exceptions (implement custom handler methods below)
    
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // TODO: Add links
        return HttpResponseBuilder.buildException(
                "This method only accepts requests with application/json",
                HttpStatus.BAD_REQUEST);
    }

}
