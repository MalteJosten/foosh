package com.vs.foosh.api.exceptions.misc;

public class IdIsNoValidUUIDException extends RuntimeException {
    
    public IdIsNoValidUUIDException(String id) {
        super("The provided id '" + id + "' is not a valid UUID!");
    }
}
