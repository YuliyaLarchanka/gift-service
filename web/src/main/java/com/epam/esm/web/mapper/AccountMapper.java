package com.epam.esm.web.mapper;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.web.dto.AccountDto;
import com.epam.esm.web.dto.AuthenticationDto;
import com.epam.esm.web.dto.AuthenticationResponseDto;
import com.epam.esm.web.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "links", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    AccountDto accountToAccountDto(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "orders", ignore = true)
    default Account authenticationDtoToAccount(AuthenticationDto auth){
        Account account = new Account();
        account.setLogin(auth.getLogin());
        account.setPassword(auth.getPassword());
        return account;
    }

    @Mapping(target = "password", ignore = true)
    default AuthenticationResponseDto accountToAuthenticationResponseDto(Account account, String token){
        AuthenticationResponseDto auth = new AuthenticationResponseDto();
        auth.setId(account.getId());
        auth.setLogin(account.getLogin());
        auth.setToken(token);
        return auth;
    }

    @Mapping(target = "orders", ignore = true)
    Account accountDtoToAccount(AccountDto accountDto);
}
