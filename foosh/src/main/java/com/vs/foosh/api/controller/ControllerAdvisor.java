package com.vs.foosh.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.device.BatchQueryNameException;
import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueQueryNameException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.misc.CouldNotDeleteCollectionException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.misc.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.misc.SavingToFileIOException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.variable.VariableList;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Device(s)
     */

    @ExceptionHandler(DeviceNameIsNotUniqueException.class)
    public ResponseEntity<Object> handleQueryNameIsNotUniqueException(DeviceNameIsNotUniqueException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DeviceNameIsNullException.class)
    public ResponseEntity<Object> handleQueryNameIsNullException(DeviceNameIsNullException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceNameIsEmptyException.class)
    public ResponseEntity<Object> handleQueryNameIsEmptyException(DeviceNameIsEmptyException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouldNotFindUniqueQueryNameException.class)
    public ResponseEntity<Object> handleCouldNotFindUniqueQueryNameException(
            CouldNotFindUniqueQueryNameException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
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
    public ResponseEntity<Object> handleDeviceIdNotFoundException(DeviceIdNotFoundException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                DeviceList.getLinks("devices"),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Environmental Variable(s)
     */

    @ExceptionHandler(VariableNotFoundException.class)
    public ResponseEntity<Object> handleVariableNotFoundException(VariableNotFoundException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                VariableList.getLinks("variables"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VariableCreationException.class)
    public ResponseEntity<Object> handleVariableCreation(VariableCreationException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                VariableList.getLinks("self"),
                HttpStatus.BAD_REQUEST);
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

    // TODO
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return HttpResponseBuilder.buildException(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
