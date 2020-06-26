package com.epam.esm.service.exception;

public class EntityToDeleteNotFoundException extends RuntimeException {
    public EntityToDeleteNotFoundException() {
    }

    public EntityToDeleteNotFoundException(String message) {
        super(message);
    }

    public EntityToDeleteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityToDeleteNotFoundException(Throwable cause) {
        super(cause);
    }
}
