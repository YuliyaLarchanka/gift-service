package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CertificateDto extends ApiDto{
    @NotNull(message = "{certificate.name.empty}")
    @Size(min = 3, max = 35, message = "{certificate.name.size}")
    private String name;

    @Size(min = 10, max = 100, message = "{certificate.description.size}")
    private String description;

    @NotNull(message = "{certificate.price.empty}")
    @DecimalMin(value = "1.0", message = "{certificate.price.decimal-min}")
    @Digits(integer = 5, fraction = 2, message = "{certificate.price.digits}")
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfCreation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfModification;

    @NotNull(message = "{certificate.duration-in-days.empty}")
    @Min(value = 1, message = "{certificate.duration-in-days.min}")
    @Max(value = 365, message = "{certificate.duration-in-days.max}")
    private Short durationInDays;

    private List<TagDto> tagList = new ArrayList<>();

    @JsonProperty("tags")
    public List<TagDto> getTagList() {
        return tagList;
    }
}
