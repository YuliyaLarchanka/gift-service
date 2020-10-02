package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AuthenticationResponseDto {
    private Long id;

    private String login;

    private String token;
}
