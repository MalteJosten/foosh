package com.vs.foosh.api.controller.advisors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.variable.MalformedVariableModelPostRequestException;
import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.exceptions.variable.VariableCreationException;
import com.vs.foosh.api.exceptions.variable.VariableDevicePostException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsEmptyException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.variable.VariableNameIsNullException;
import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.exceptions.variable.VariablePatchException;
import com.vs.foosh.api.exceptions.variable.VariablePredictionException;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

@ControllerAdvice
public class VariableAdvisor extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(VariableDevicePostException.class)
    public ResponseEntity<Object> handleVariableDevicePostException(VariableDevicePostException exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(ListService.getVariableList().getThing(exception.getVariableId()).getSelfLinks());
        links.addAll(ListService.getVariableList().getThing(exception.getVariableId()).getVarDeviceLinks());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                exception.getStatus());
    }

    @ExceptionHandler(VariablePatchException.class)
    public ResponseEntity<Object> handleVariablePatchException(VariablePatchException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getVariableLinkBlock(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedVariableModelPostRequestException.class)
    public ResponseEntity<Object> handleMalformedVariableModelPostRequestException(MalformedVariableModelPostRequestException exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(LinkBuilder.getVariableLinkBlock(exception.getId()));
        links.addAll(ListService.getVariableList().getThing(exception.getId()).getVarModelLinks());

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedVariablePredictionRequest.class)
    public ResponseEntity<Object> handleMalformedVariablePredicitonRequestException(MalformedVariablePredictionRequest exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(LinkBuilder.getVariableLinkBlock(exception.getId()));

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VariablePredictionException.class)
    public ResponseEntity<Object> handleVariablePredictionException(VariablePredictionException exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(LinkBuilder.getVariableLinkBlock(exception.getId().toString()));

        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
