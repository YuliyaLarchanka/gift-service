package com.epam.esm.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TagDto {
    private Long id;

    @NotBlank(message = "Name can't be blank")
    @Size(min = 2, max = 35)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
