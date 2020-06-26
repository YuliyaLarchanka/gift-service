package com.epam.esm.web.dto;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ErrorDto {
    private String message;
    private List<FieldError> fieldErrors = new ArrayList<>();
    private List<String> parameterErrors = new ArrayList<>();

    public ErrorDto(String message) {
        this.message = message;
    }

    public ErrorDto(String message, List<FieldError> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<String> getParameterErrors() {
        return parameterErrors;
    }

    public void setParameterErrors(List<String> parameterErrors) {
        this.parameterErrors = parameterErrors;
    }
}
