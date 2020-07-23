package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto {
    private Long id;

    @NotBlank(message = "Name can't be blank")
    @Size(min = 2, max = 35)
    private String name;

    private LinksDto links;

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

    public LinksDto getLinks() {
        return links;
    }

    public void setLinks(LinksDto links) {
        this.links = links;
    }
}
