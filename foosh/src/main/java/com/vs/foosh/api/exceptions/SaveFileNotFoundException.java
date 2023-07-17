package com.vs.foosh.api.exceptions;

public class SaveFileNotFoundException extends RuntimeException {
    public SaveFileNotFoundException() {
        super("Could not find save file.");   
    }
}
