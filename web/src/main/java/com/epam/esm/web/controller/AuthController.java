package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.service.AccountService;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.dto.AuthenticationDto;
import com.epam.esm.web.dto.AuthenticationResponseDto;
import com.epam.esm.web.mapper.AccountMapper;
import com.epam.esm.web.security.JwtProvider;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
public class AuthController {
    private final AccountService accountService;
    private final JwtProvider jwtProvider;
    private final AccountMapper accountMapper;

    public AuthController(AccountService accountService, JwtProvider jwtProvider, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.jwtProvider = jwtProvider;
        this.accountMapper = accountMapper;
    }

    @PostMapping("/auth")
    public AuthenticationResponseDto auth(@Valid @RequestBody AuthenticationDto authenticationDto) {
        Account account = accountMapper.authenticationDtoToAccount(authenticationDto);
        Optional<Account> accountOptional = accountService.findByLoginAndPassword(account.getLogin(), account.getPassword());
        if (accountOptional.isEmpty()){
            throw new EntityNotFoundException();
        }
        account = accountOptional.get();
        String token = jwtProvider.generateToken(account.getLogin());
        return accountMapper.accountToAuthenticationResponseDto(account, token);
    }
}
