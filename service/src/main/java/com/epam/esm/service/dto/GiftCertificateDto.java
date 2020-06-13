package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateDto {
    private long id;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Description can't be blank")
    private String description;
    @NotNull(message = "Price should be presented")
    @Min(value = 0, message = "Price can't be lower than zero")
    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfCreation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfModification;
    @Min(value = 1, message = "Duration can't be less than a day")
    private short durationInDays;
    private List<TagDto> tagDtoList = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public LocalDateTime getDateOfModification() {
        return dateOfModification;
    }

    public void setDateOfModification(LocalDateTime dateOfModification) {
        this.dateOfModification = dateOfModification;
    }

    public short getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(short durationInDays) {
        this.durationInDays = durationInDays;
    }

    @JsonProperty("tags")
    public List<TagDto> getTagDtoList() {
        return tagDtoList;
    }

    public void setTagDtoList(List<TagDto> tagDtoList) {
        this.tagDtoList = tagDtoList;
    }
}
