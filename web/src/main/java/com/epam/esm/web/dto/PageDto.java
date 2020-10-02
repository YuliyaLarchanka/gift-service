package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PageDto<T> {
    private int totalCount;
    private int offset;
    private boolean hasNext;
    private boolean hasPrevious;
    private List<T> content;
    private LinksDto links;
}
