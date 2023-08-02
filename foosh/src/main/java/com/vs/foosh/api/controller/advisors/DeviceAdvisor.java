package com.vs.foosh.api.controller.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

@ControllerAdvice
public class DeviceAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IdIsNoValidUUIDException.class)
    public ResponseEntity<Object> handleIdIsNoValidUUIDException(IdIsNoValidUUIDException exception,
            WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getDeviceList().getLinks("devices"),
                HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(DeviceIdNotFoundException.class)
    public ResponseEntity<Object> handleDeviceIdNotFoundException(DeviceIdNotFoundException exception, WebRequest request) {

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                ListService.getDeviceList().getLinks("devices"),
                HttpStatus.NOT_FOUND);
    }

}
