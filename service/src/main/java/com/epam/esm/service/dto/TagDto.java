package com.epam.esm.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TagDto {
    private long id;

    @NotBlank(message = "Name can't be blank")
    @Size(min = 2, max = 35)
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
