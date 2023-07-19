package com.vs.foosh.api.exceptions.misc;

public class CouldNotDeleteCollectionException extends RuntimeException {
    public CouldNotDeleteCollectionException() {
        super("Could not delete collection!");
    }  
}
