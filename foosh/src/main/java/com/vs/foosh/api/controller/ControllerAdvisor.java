package com.vs.foosh.api.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.exceptions.*;
import com.vs.foosh.api.services.HttpResponseBuilder;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Device(s)
     */

    @ExceptionHandler(QueryNameIsNotUniqueException.class)
    public ResponseEntity<Object> handleQueryNameIsNotUniqueException(QueryNameIsNotUniqueException exception,
            WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getDeviceLink(exception.getId()));
        linkBlock.put("devices", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(QueryNameIsNullException.class)
    public ResponseEntity<Object> handleQueryNameIsNullException(QueryNameIsNullException exception,
            WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getDeviceLink(exception.getId()));
        linkBlock.put("devices", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QueryNameIsEmptyException.class)
    public ResponseEntity<Object> handleQueryNameIsEmptyException(QueryNameIsEmptyException exception,
            WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getDeviceLink(exception.getId()));
        linkBlock.put("devices", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouldNotFindUniqueQueryNameException.class)
    public ResponseEntity<Object> handleCouldNotFindUniqueQueryNameException(
            CouldNotFindUniqueQueryNameException exception, WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getDeviceLink(exception.getId()));
        linkBlock.put("devices", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BatchQueryNameException.class)
    public ResponseEntity<Object> handleBatchQueryNameException(BatchQueryNameException exception, WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("devices", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceIdNotFoundException.class)
    public ResponseEntity<Object> handleDeviceIdNotFoundException(DeviceIdNotFoundException exception,
            WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("devices", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.NOT_FOUND);
    }

    /**
     * Environmental Variable(s)
     */

    @ExceptionHandler(EnvironmentalVariableNotFoundException.class)
    public ResponseEntity<Object> handleEnvironmentalVariableNotFoundException(
            EnvironmentalVariableNotFoundException exception, WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("variables", LinkBuilder.getVariableListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.NOT_FOUND);
    }

    /**
     * Utility
     */

    // TODO: Generalize so it also can be used for other entities.
    @ExceptionHandler(IdIsNoValidUUIDException.class)
    public ResponseEntity<Object> handleIdIsnoValidUUIDException(IdIsNoValidUUIDException exception,
            WebRequest request) {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("devices", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                linkBlock,
                HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(HttpMappingNotImplementedException.class)
    public ResponseEntity<Object> handleHttpMappingNotImplementedExeption(HttpMappingNotImplementedException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                exception.getReturnPath(),
                HttpStatus.NOT_IMPLEMENTED);
    }

}
