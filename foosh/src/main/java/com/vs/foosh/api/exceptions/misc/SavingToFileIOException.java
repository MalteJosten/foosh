package com.vs.foosh.api.exceptions.misc;

public class SavingToFileIOException extends RuntimeException {
    public SavingToFileIOException() {
        super("An error occurred while trying to write save file.");
    }
}
