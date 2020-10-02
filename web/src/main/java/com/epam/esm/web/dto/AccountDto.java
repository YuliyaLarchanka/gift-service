package com.epam.esm.web.dto;

import com.epam.esm.repository.entity.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AccountDto extends ApiDto{
    @NotNull(message = "{login.empty}")
    @Size(min = 3, max = 20, message = "{login.size}")
    private String login;

    @NotNull(message = "{password.empty}")
    @Size(min = 5, max = 20, message = "{password.size}")
    private String password;

    private RoleEnum role;
}
