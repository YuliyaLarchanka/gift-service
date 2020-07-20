package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.service.AccountService;
import com.epam.esm.web.dto.AccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public AccountController(AccountService accountService, ModelMapper modelMapper) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDto create(@Valid @RequestBody AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        Account createdAccount = accountService.create(account);
        return modelMapper.map(createdAccount, AccountDto.class);
    }
}
