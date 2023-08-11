package com.vs.foosh.api.controller.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsNullException;
import com.vs.foosh.api.services.HttpResponseBuilder;

@ControllerAdvice
public class FooSHJsonPatchAdvisor {
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

    @ExceptionHandler(FooSHJsonPatchOperationException.class)
    public ResponseEntity<Object> handleFooSHJsonPatchOperationException(FooSHJsonPatchOperationException exception,
                    WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                exception.getLinks(),
                HttpStatus.BAD_REQUEST);
    }
}
