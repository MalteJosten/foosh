package com.vs.foosh.api.controller.advisors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.services.HttpResponseBuilder;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringAdvisor extends ResponseEntityExceptionHandler {
    
    ///
    /// Spring Boot Overrides
    ///

    // TODO: Test each API Endpoint and make sure to cover all @RequestBody exceptions (implement custom handler methods below)
    // TODO: Add links

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return HttpResponseBuilder.buildException(
                "This method only accepts requests with Content-Type: 'application/json'",
                HttpStatus.BAD_REQUEST);
    }

    @Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return HttpResponseBuilder.buildException(
                "Failed to read request. Please refer to the documentation for the valid type of request(s).",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return HttpResponseBuilder.buildException(
                "This method only accepts requests with Content-Type: 'application/json'",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return HttpResponseBuilder.buildException(
                "The path argument '" + ex.getName() + "' should be of type " + ex.getRequiredType().getName(),
                HttpStatus.BAD_REQUEST);
    }

}
