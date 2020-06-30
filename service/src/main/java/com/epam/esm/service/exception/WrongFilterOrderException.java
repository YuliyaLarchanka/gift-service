package com.epam.esm.service.exception;

public class WrongFilterOrderException extends RuntimeException{
    public WrongFilterOrderException() {
    }

    public WrongFilterOrderException(String message) {
        super(message);
    }

    public WrongFilterOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongFilterOrderException(Throwable cause) {
        super(cause);
    }
}
