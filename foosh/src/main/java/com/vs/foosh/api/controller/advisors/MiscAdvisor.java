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

@ControllerAdvice
public class MiscAdvisor {

    @ExceptionHandler(HttpMappingNotAllowedException.class)
    public ResponseEntity<Object> handleHttpMappingNotAllowedExeption(HttpMappingNotAllowedException exception,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problemDetail.setProperty("_links", exception.getLinks());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

    @ExceptionHandler({
        SaveFileNotFoundException.class,
        SavingToFileIOException.class,
        CouldNotDeleteCollectionException.class})
    public ResponseEntity<Object> handleSaveFileExceptions(FooSHSaveFileException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());

        return ResponseEntity.status(exception.getStatus()).body(problemDetail);
    }

}
