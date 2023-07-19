package com.vs.foosh.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.*;
import com.vs.foosh.api.model.DeviceList;
import com.vs.foosh.api.model.EnvironmentVariableList;
import com.vs.foosh.api.model.LinkEntry;
import com.vs.foosh.api.services.HttpResponseBuilder;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Device(s)
     */

    @ExceptionHandler(QueryNameIsNotUniqueException.class)
    public ResponseEntity<Object> handleQueryNameIsNotUniqueException(QueryNameIsNotUniqueException exception,
            WebRequest request) {

        List<LinkEntry> links = DeviceList.getDevice(exception.getId().toString()).getSelfLinks();
        links.addAll(DeviceList.getLinks("devices"));

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(QueryNameIsNullException.class)
    public ResponseEntity<Object> handleQueryNameIsNullException(QueryNameIsNullException exception,
            WebRequest request) {

        List<LinkEntry> links = DeviceList.getDevice(exception.getId().toString()).getSelfLinks();
        links.addAll(DeviceList.getLinks("devices"));

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QueryNameIsEmptyException.class)
    public ResponseEntity<Object> handleQueryNameIsEmptyException(QueryNameIsEmptyException exception,
            WebRequest request) {

        List<LinkEntry> links = DeviceList.getDevice(exception.getId().toString()).getSelfLinks();
        links.addAll(DeviceList.getLinks("devices"));


        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouldNotFindUniqueQueryNameException.class)
    public ResponseEntity<Object> handleCouldNotFindUniqueQueryNameException(
            CouldNotFindUniqueQueryNameException exception, WebRequest request) {

        List<LinkEntry> links = DeviceList.getDevice(exception.getId().toString()).getSelfLinks();
        links.addAll(DeviceList.getLinks("devices"));

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BatchQueryNameException.class)
    public ResponseEntity<Object> handleBatchQueryNameException(BatchQueryNameException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                DeviceList.getLinks("devices"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceIdNotFoundException.class)
    public ResponseEntity<Object> handleDeviceIdNotFoundException(DeviceIdNotFoundException exception,
            WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                DeviceList.getLinks("devices"),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Environmental Variable(s)
     */

    @ExceptionHandler(EnvironmentalVariableNotFoundException.class)
    public ResponseEntity<Object> handleEnvironmentalVariableNotFoundException(
            EnvironmentalVariableNotFoundException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                EnvironmentVariableList.getLinks("variables"),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Utility
     */

    // TODO: Generalize so it also can be used for other entities.
    @ExceptionHandler(IdIsNoValidUUIDException.class)
    public ResponseEntity<Object> handleIdIsNoValidUUIDException(IdIsNoValidUUIDException exception,
            WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                DeviceList.getLinks("devices"),
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

    @ExceptionHandler(HttpMappingNotAllowedException.class)
    public ResponseEntity<Object> handleHttpMappingNotAllowedExeption(HttpMappingNotAllowedException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                exception.getReturnPaths(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(SaveFileNotFoundException.class)
    public ResponseEntity<Object> handleSaveFileNotFoundException(SaveFileNotFoundException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SavingToFileIOException.class)
    public ResponseEntity<Object> handleSavingToFileIOException(SavingToFileIOException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CouldNotDeleteCollectionException.class)
    public ResponseEntity<Object> handleCouldNotDeleteCollection(CouldNotDeleteCollectionException exception,
            WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
