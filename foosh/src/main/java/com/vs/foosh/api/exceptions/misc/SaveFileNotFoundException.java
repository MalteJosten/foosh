package com.vs.foosh.api.exceptions.misc;

public class SaveFileNotFoundException extends RuntimeException {
    public SaveFileNotFoundException() {
        super("Could not find save file.");   
    }
}
