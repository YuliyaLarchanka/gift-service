package com.epam.esm.web.dto;

import com.epam.esm.repository.entity.RoleEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AccountDto {
    private Long id;

    @NotBlank(message = "Login can't be blank")
    @Size(min = 2, max = 20)
    private String login;

    private RoleEnum role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
