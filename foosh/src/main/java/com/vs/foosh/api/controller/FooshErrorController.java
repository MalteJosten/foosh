package com.vs.foosh.api.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vs.foosh.api.services.helpers.LinkBuilderService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * A {@link Controller} that extends Spring's {@link ErrorController} and replaces the standard Spring Whitelabel Error Page ({@code /error}).
 * 
 * For this to properly work, {@code server.error.whitelabel.enabled} should be set to {@code false} in {@code application.properties}.
 */
@Controller
public class FooshErrorController implements ErrorController {

    /**
     * Handle all not handled exceptions/errors and display a fitting {@link ProblemDetail}.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
     * 
     * @return a {@link ResponseEntity} with the constructed {@link ProblemDetail} as its body
     */
    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "An error occurred!";
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (status != null) {
            HttpStatus statusCode = HttpStatus.valueOf(Integer.valueOf(status.toString()));

            if (statusCode == HttpStatus.NOT_FOUND) {
                httpStatus = HttpStatus.NOT_FOUND;
                message = "Page not found!";
            }
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, message);
        problemDetail.setDetail(message);
        problemDetail.setProperty("_links", LinkBuilderService.getRootLinkEntries());

        return ResponseEntity.status(httpStatus).body(problemDetail);
    }
}
