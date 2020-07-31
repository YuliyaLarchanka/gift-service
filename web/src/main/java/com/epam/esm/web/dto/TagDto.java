package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto extends ApiDto{
    @NotBlank(message = "Name can't be blank. ")
    @Size(min = 2, max = 35, message = "Name can't be less than 2 and more than 35. ")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
