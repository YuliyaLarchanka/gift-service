package com.epam.esm.repository.entity;

import lombok.Data;

@Data
public class Links {
    private String self;
    private String previous;
    private String next;
}
