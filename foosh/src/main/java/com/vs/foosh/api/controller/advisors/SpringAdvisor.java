package com.vs.foosh.api.controller.advisors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A {@link ControllerAdvice} which overrides Spring's own exception handling by extending {@link ResponseEntityExceptionHandler}.
 * We also need to use the {@link Order} annotation to take presedence over Spring's {@link ResponseEntityExceptionHandler}.
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Construct and return a {@link ProblemDetail} if a {@link HttpMediaTypeNotSupportedException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param ex the thrown {@link HttpMediaTypeNotSupportedException}
	 * @param headers the {@link HttpHeaders} to use for the response
	 * @param status the {@link HttpStatusCode} to use for the response
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            status,
            "This method does not allow the Content-Type: '" + ex.getContentType() + "'. Please refer to the documentation for the allowed Content-Type(s).");
        problemDetail.setTitle("HttpMediaTypeNotSupportedException");

        return ResponseEntity.status(status).body(problemDetail);
    }

    /**
     * Construct and return a {@link ProblemDetail} if a {@link HttpMessageNotReadableException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param ex the thrown {@link HttpMessageNotReadableException}
	 * @param headers the {@link HttpHeaders} to use for the response
	 * @param status the {@link HttpStatusCode} to use for the response
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            status,
            "Failed to read request. Please refer to the documentation for the valid type of request(s).");
        problemDetail.setTitle("HttpMessageNotReadableException");

        return ResponseEntity.status(status).body(problemDetail);
    }

    /**
     * Construct and return a {@link ProblemDetail} if a {@link MethodArgumentTypeMismatchException} is thrown.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @param ex the thrown {@link MethodArgumentTypeMismatchException}
     * @param request the {@link WebRequest} which triggered the exception
     * 
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} as its body.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            status,
            "The path argument '" + ex.getName() + "' should be of type " + ex.getRequiredType().getName());
        problemDetail.setTitle("MethodArgumentTypeMismatchException");

        return ResponseEntity.status(status).body(problemDetail);
    }

}
