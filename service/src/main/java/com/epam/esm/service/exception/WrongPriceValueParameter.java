package com.epam.esm.service.exception;

public class WrongPriceValueParameter extends RuntimeException{
    public WrongPriceValueParameter() {
    }

    public WrongPriceValueParameter(String message) {
        super(message);
    }

    public WrongPriceValueParameter(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPriceValueParameter(Throwable cause) {
        super(cause);
    }
}
