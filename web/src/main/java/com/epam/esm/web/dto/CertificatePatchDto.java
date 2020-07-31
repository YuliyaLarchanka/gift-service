package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CertificatePatchDto {
    private Long id;

    @Size(min = 2, max = 35, message = "Name can't be less than 2 and more than 35 letters. ")
    private String name;

    @Size(min = 10, max = 60, message = "Description can't be less than 10 and more than 60 letters. ")
    private String description;

    @DecimalMin(value = "1.0", message = "Price can't be lower than 1$. ")
    @Digits(integer = 5, fraction = 2, message = "Price can't be more than 99999.99 .And only 2 values after point.")
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfCreation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfModification;

    @Min(value = 1, message = "Duration can't be less than a day. ")
    @Max(value = 365, message = "Duration can't be more than a year(365 days). ")
    private Short durationInDays;

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

    public Short getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Short durationInDays) {
        this.durationInDays = durationInDays;
    }
}
