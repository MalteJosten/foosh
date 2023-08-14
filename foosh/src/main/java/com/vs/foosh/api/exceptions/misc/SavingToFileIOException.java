package com.vs.foosh.api.exceptions.misc;

import org.springframework.http.HttpStatus;

public class SavingToFileIOException extends FooSHSaveFileException {

    public SavingToFileIOException(String collection) {
        super("An error occurred while trying to write save file for collection '" + collection + "'.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
