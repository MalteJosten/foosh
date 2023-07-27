package com.vs.foosh.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.device.BatchDeviceNameException;
import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.exceptions.misc.CouldNotDeleteCollectionException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.misc.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.misc.SavingToFileIOException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.exceptions.variable.BatchVariableNameException;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsEmptyException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNullException;
import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.exceptions.variable.VariablePatchException;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Device(s)
     */

    @ExceptionHandler(DeviceNameIsNotUniqueException.class)
    public ResponseEntity<Object> handleDeviceNameIsNotUniqueException(DeviceNameIsNotUniqueException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DeviceNameIsNullException.class)
    public ResponseEntity<Object> handleDeviceNameIsNullException(DeviceNameIsNullException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceNameIsEmptyException.class)
    public ResponseEntity<Object> handleDeviceNameIsEmptyException(DeviceNameIsEmptyException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouldNotFindUniqueDeviceNameException.class)
    public ResponseEntity<Object> handleCouldNotFindUniqueDeviceNameException(
            CouldNotFindUniqueDeviceNameException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BatchDeviceNameException.class)
    public ResponseEntity<Object> handleBatchDeviceNameException(BatchDeviceNameException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getAbstractDeviceList().getLinks("devices"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceIdNotFoundException.class)
    public ResponseEntity<Object> handleDeviceIdNotFoundException(DeviceIdNotFoundException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getAbstractDeviceList().getLinks("devices"),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Variable(s)
     */

    @ExceptionHandler(VariableNotFoundException.class)
    public ResponseEntity<Object> handleVariableNotFoundException(VariableNotFoundException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getVariableList().getLinks("variables"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VariableCreationException.class)
    public ResponseEntity<Object> handleVariableCreation(VariableCreationException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getVariableList().getLinks("self"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VariableNameIsNullException.class)
    public ResponseEntity<Object> handleVariableNameIsNullException(VariableNameIsNullException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getDeviceLinkWithDevices(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VariableNameIsEmptyException.class)
    public ResponseEntity<Object> handleVariableNameIsEmptyException(VariableNameIsEmptyException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getVariableLinkBlock(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VariableNameIsNotUniqueException.class)
    public ResponseEntity<Object> handleVariableNameIsNotUniqueException(VariableNameIsNotUniqueException exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();

        if (exception.getId() == null) {
                links = ListService.getVariableList().getLinks("variables");
        } else {
                links = LinkBuilder.getVariableLinkBlock(exception.getId().toString());
        }

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VariableNameMustNotBeAnUuidException.class)
    public ResponseEntity<Object> handleVariableNameMustNotBeAnUUidException(VariableNameMustNotBeAnUuidException exception,
            WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getVariableLinkBlock(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BatchVariableNameException.class)
    public ResponseEntity<Object> handleBatchVariableNameException(BatchVariableNameException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getVariableList().getLinks("self"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VariableDevicePostException.class)
    public ResponseEntity<Object> handleVariableDevicePostException(VariableDevicePostException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getVariableList().getVariable(exception.getVariableId().toString()).getDeviceLinks(),
                exception.getStatus());
    }

    @ExceptionHandler(VariablePatchException.class)
    public ResponseEntity<Object> handleVariablePatchException(VariablePatchException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getVariableLinkBlock(exception.getId().toString()),
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
                ListService.getAbstractDeviceList().getLinks("devices"),
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

    ///
    /// Spring Boot Overrides
    ///
    
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // TODO: Add links
        return HttpResponseBuilder.buildException(
                "This method only accepts requests with application/json",
                HttpStatus.BAD_REQUEST);
    }

}
