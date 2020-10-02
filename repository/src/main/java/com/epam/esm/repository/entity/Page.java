package com.epam.esm.repository.entity;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private int totalCount;
    private int offset;
    private boolean hasNext;
    private boolean hasPrevious;
    private List<T> content;
    private Links links;
}
