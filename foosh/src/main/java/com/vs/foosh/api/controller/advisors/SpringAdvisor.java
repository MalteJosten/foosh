package com.vs.foosh.api.controller.advisors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringAdvisor extends ResponseEntityExceptionHandler {
    
    ///
    /// Spring Boot Overrides
    ///

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(
            status,
            "This method does not allow the Content-Type: '" + ex.getContentType() + "'. Please refer to the documentation for the allowed Content-Type(s)."));
    }

    @Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(
            status,
            "Failed to read request. Please refer to the documentation for the valid type of request(s)."));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(
            status,
            "This method only accepts requests with Content-Type: 'application/json'"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return ResponseEntity.status(400).body(ProblemDetail.forStatusAndDetail(
            HttpStatusCode.valueOf(400),
            "The path argument '" + ex.getName() + "' should be of type " + ex.getRequiredType().getName()));
    }

}
