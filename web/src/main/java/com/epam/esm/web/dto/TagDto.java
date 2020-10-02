package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TagDto extends ApiDto{
    @NotNull(message = "{tag-name.empty}")
    @Size(min = 3, max = 15, message = "{tag-name.size}")
    private String name;
}
