package com.vs.foosh.api.exceptions;

public class CouldNotDeleteCollectionException extends RuntimeException {
    public CouldNotDeleteCollectionException() {
        super("Could not delete collection!");
    }  
}
