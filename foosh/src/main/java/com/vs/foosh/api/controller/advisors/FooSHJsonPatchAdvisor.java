package com.vs.foosh.api.controller.advisors;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalPathException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsNullException;

@ControllerAdvice
public class FooSHJsonPatchAdvisor {
    @ExceptionHandler({
        FooSHJsonPatchFormatException.class,
        FooSHJsonPatchIllegalPathException.class,
        FooSHJsonPatchIllegalOperationException.class,
        FooSHJsonPatchValueException.class,
        FooSHJsonPatchValueIsEmptyException.class,
        FooSHJsonPatchValueIsNullException.class
    })
    public ResponseEntity<Object> handleFooSHJsonPatchExceptions(FooSHJsonPatchException exception,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getMessage());

        return ResponseEntity.status(exception.getStatusCode()).body(problemDetail);
    }

    @ExceptionHandler(FooSHJsonPatchOperationException.class)
    public ResponseEntity<Object> handleFooSHJsonPatchOperationException(FooSHJsonPatchOperationException exception,
                    WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getMessage());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatusCode()).body(problemDetail);
    }
}
