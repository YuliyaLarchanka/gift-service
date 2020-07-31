package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationDto {
    @NotBlank(message = "Login can't be blank")
    @Size(min = 2, max = 20)
    private String login;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 2, max = 20)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
