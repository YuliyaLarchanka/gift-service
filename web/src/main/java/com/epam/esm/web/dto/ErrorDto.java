package com.epam.esm.web.dto;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ErrorDto {
    private Integer code;
    private String message;
    private List<FieldError> fieldErrors = new ArrayList<>();
    private List<String> parameterErrors = new ArrayList<>();

    public ErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorDto(int code, String message, List<FieldError> fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
