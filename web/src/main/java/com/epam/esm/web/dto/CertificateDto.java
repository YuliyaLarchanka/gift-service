package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDto extends ApiDto{
    @NotBlank(message = "Name can't be blank. ")
    @Size(min = 2, max = 35, message = "Name can't be less than 2 and more than 35 letters. ")
    private String name;

    @Size(min = 10, max = 60, message = "Description can't be less than 10 and more than 60 letters. ")
    private String description;

    @NotNull(message = "Price should be presented. ")
    @DecimalMin(value = "1.0", message = "Price can't be lower than 1$. ")
    @Digits(integer = 5, fraction = 2, message = "Price can't be more than 99999.99 .And only 2 values after point.")
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfCreation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfModification;

    @NotNull(message = "Duration in days should be presented. ")
    @Min(value = 1, message = "Duration can't be less than a day. ")
    @Max(value = 365, message = "Duration can't be more than a year(365 days). ")
    private Short durationInDays;

    private List<TagDto> tagList = new ArrayList<>();

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

    @JsonProperty("tags")
    public List<TagDto> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDto> tagList) {
        this.tagList = tagList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateDto that = (CertificateDto) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (dateOfCreation != null ? !dateOfCreation.equals(that.dateOfCreation) : that.dateOfCreation != null)
            return false;
        if (dateOfModification != null ? !dateOfModification.equals(that.dateOfModification) : that.dateOfModification != null)
            return false;
        if (durationInDays != null ? !durationInDays.equals(that.durationInDays) : that.durationInDays != null)
            return false;
        return tagList != null ? tagList.equals(that.tagList) : that.tagList == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (dateOfCreation != null ? dateOfCreation.hashCode() : 0);
        result = 31 * result + (dateOfModification != null ? dateOfModification.hashCode() : 0);
        result = 31 * result + (durationInDays != null ? durationInDays.hashCode() : 0);
        result = 31 * result + (tagList != null ? tagList.hashCode() : 0);
        return result;
    }
}
