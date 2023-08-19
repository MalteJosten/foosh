package com.vs.foosh.api.exceptions.misc;

import java.nio.file.Path;

public class SavingDirectoryException extends RuntimeException {
    public SavingDirectoryException(Path location) {
        super("[ERROR] An error occurred while trying to create save directory at " + location.toAbsolutePath());
    } 
}
