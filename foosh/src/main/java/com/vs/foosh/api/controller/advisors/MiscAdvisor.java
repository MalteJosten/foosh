package com.vs.foosh.api.controller.advisors;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.vs.foosh.api.exceptions.misc.CouldNotDeleteCollectionException;
import com.vs.foosh.api.exceptions.misc.FooSHSaveFileException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.misc.SavingToFileIOException;

/**
 * A {@link ControllerAdvice} which intercepts {@link HttpMappingNotAllowedException}s and exceptions thrown by {@link PersistentDataService} and handles them correctly.
 */
@ControllerAdvice
public class MiscAdvisor {

    /**
     * Construct and return a {@link ProblemDetail} if a {@link HttpMappingNotAllowedException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param exception the thrown {@link HttpMappingNotAllowedException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @ExceptionHandler(HttpMappingNotAllowedException.class)
    public ResponseEntity<Object> handleHttpMappingNotAllowedExeption(HttpMappingNotAllowedException exception,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setProperty("_links", exception.getLinks());
        problemDetail.setTitle(exception.getName());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

    /**
     * Construct and return a {@link ProblemDetail} for exceptions thrown by the {@link PersistentDataService}.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param exception the thrown {@link FooSHSaveFileException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @ExceptionHandler({
        SaveFileNotFoundException.class,
        SavingToFileIOException.class,
        CouldNotDeleteCollectionException.class
    })
    public ResponseEntity<Object> handleSaveFileExceptions(FooSHSaveFileException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setTitle(exception.getName());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

}
