package com.vs.foosh.api.exceptions.misc;

public class SavingToFileIOException extends RuntimeException {
    public SavingToFileIOException(String collection) {
        super("An error occurred while trying to write save file for collection '" + collection + "'.");
    }
}
