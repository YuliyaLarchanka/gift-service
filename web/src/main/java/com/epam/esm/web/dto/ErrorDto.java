package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorDto {
    private Integer code;
    private String message;
    private List<String> parameterErrors = new ArrayList<>();

    public ErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
