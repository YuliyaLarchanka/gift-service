package com.epam.esm.web.dto;

import com.epam.esm.repository.entity.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto extends ApiDto{
    @NotBlank(message = "Login can't be blank")
    @Size(min = 2, max = 20)
    private String login;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 6, max = 20)
    private String password;

    private RoleEnum role;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
