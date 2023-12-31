package com.vs.foosh.api.exceptions.misc;

public class SaveFileIsNotAFileException extends RuntimeException {
    public SaveFileIsNotAFileException(String path) {
        super("[ERROR] The given save file path " + path + " is not a file!");
    }
}
