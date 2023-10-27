package com.vs.foosh.api.exceptions.misc;

import org.springframework.http.HttpStatus;

public class SaveFileNotFoundException extends FooSHSaveFileException {

    public SaveFileNotFoundException(String collection) {
        super("Could not find save file for collection '" + collection + "'!", HttpStatus.INTERNAL_SERVER_ERROR);   
        super.name = "SaveFileNotFoundException";
    }
}
