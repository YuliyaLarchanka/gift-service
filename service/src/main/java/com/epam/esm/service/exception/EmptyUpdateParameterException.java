package com.epam.esm.service.exception;

public class EmptyUpdateParameterException extends RuntimeException{
    public EmptyUpdateParameterException() {
    }

    public EmptyUpdateParameterException(String message) {
        super(message);
    }

    public EmptyUpdateParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyUpdateParameterException(Throwable cause) {
        super(cause);
    }
}
