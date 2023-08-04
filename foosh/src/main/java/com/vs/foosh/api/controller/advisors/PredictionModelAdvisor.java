package com.vs.foosh.api.controller.advisors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vs.foosh.api.exceptions.predictionModel.CouldNotFindVariableParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingDeviceException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNotFoundException;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.HttpResponseBuilder;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

@ControllerAdvice
public class PredictionModelAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MalformedParameterMappingException.class)
    public ResponseEntity<Object> handleMalformedParameterMappingException(MalformedParameterMappingException exception, WebRequest request) {
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getPredictionModelLinkBlock(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouldNotFindVariableParameterMappingException.class)
    public ResponseEntity<Object> handleCouldNotFindVariableParameterMappingException(CouldNotFindVariableParameterMappingException exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(LinkBuilder.getPredictionModelLinkBlock(exception.getId().toString()));
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.GET, List.of()));
        
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                LinkBuilder.getPredictionModelLinkBlock(exception.getId().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterMappingDeviceException.class)
    public ResponseEntity<Object> handleParameterMappingDeviceException(ParameterMappingDeviceException exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(LinkBuilder.getPredictionModelLinkBlock(exception.getModelId().toString()));
        links.addAll(ListService.getVariableList().getThing(exception.getVariableId().toString()).getSelfStaticLinks("variable"));
        links.addAll(ListService.getDeviceList().getThing(exception.getDeviceId().toString()).getSelfStaticLinks("device"));
        
        return HttpResponseBuilder.buildException(
                exception.getMessage(),
                links,
                HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(PredictionModelNotFoundException.class)
    public ResponseEntity<Object> handlePredictionModelNotFoundException(PredictionModelNotFoundException exception, WebRequest request) {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(ListService.getPredictionModelList().getLinks("models"));
        
        return HttpResponseBuilder.buildException(
                "Could not find prediction model with identifier '" + exception.getId() + "'!",
                links,
                HttpStatus.BAD_REQUEST);
    }
}
