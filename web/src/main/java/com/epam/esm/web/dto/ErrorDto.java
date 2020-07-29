package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {
    private Integer code;
    private String message;
    private List<String> parameterErrors = new ArrayList<>();

    public ErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
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

    public List<String> getParameterErrors() {
        return parameterErrors;
    }

    public void setParameterErrors(List<String> parameterErrors) {
        this.parameterErrors = parameterErrors;
    }
}
