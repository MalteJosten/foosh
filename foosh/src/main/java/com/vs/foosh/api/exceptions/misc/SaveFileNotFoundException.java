package com.vs.foosh.api.exceptions.misc;

public class SaveFileNotFoundException extends RuntimeException {
    public SaveFileNotFoundException(String collection) {
        super("Could not find save file for collection '" + collection + "'!");   
    }
}
