package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AuthenticationDto {
    @NotNull(message = "{login.empty}")
    @Size(min = 3, max = 20, message = "{login.size}")
    private String login;

    @NotNull(message = "{password.empty}")
    @Size(min = 5, max = 20, message = "{password.size}")
    private String password;
}
