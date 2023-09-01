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

/**
 * A {@link ControllerAdvice} which intercepts FooSHJsonPatch exceptions and handles them correctly.
 */
@ControllerAdvice
public class FooSHJsonPatchAdvisor {

    /**
     * Construct and return a {@link ProblemDetail} without links.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param exception the thrown {@link FooSHJsonPatchException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
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

    /**
     * Construct and return a {@link ProblemDetail} with links.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>

     * @param exception the thrown {@link FooSHJsonPatchOperationException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @ExceptionHandler(FooSHJsonPatchOperationException.class)
    public ResponseEntity<Object> handleFooSHJsonPatchOperationException(FooSHJsonPatchOperationException exception,
                    WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getMessage());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatusCode()).body(problemDetail);
    }
}
