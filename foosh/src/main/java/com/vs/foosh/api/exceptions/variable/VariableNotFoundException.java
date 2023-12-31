package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.exceptions.misc.FooSHApiException;
import com.vs.foosh.api.services.helpers.ListService;

public class VariableNotFoundException extends FooSHApiException {

    public VariableNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND);
        super.name = "VariableNotFoundException";

        this.links.addAll(ListService.getVariableList().getLinks("variables"));

        try {
            UUID uniqueId = UUID.fromString(id);
            this.message = "Could not find variable with id '" + uniqueId + "'!";
        } catch (IllegalArgumentException e) {
            this.message = "Could not find variable with name '" + id.toLowerCase() + "'!";
        }
    }
}
