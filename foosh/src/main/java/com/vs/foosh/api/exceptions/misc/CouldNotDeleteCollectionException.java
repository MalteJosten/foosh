package com.vs.foosh.api.exceptions.misc;

import org.springframework.http.HttpStatus;

public class CouldNotDeleteCollectionException extends FooSHSaveFileException {

    public CouldNotDeleteCollectionException() {
        super("Could not delete collection!", HttpStatus.INTERNAL_SERVER_ERROR);
        super.name = "CouldNotDeleteCollectionException";
    }  
}
