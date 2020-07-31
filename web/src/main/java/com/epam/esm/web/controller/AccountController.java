package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.service.AccountService;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.dto.AccountDto;
import com.epam.esm.web.dto.PageDto;
import com.epam.esm.web.mapper.AccountMapper;
import com.epam.esm.web.mapper.PageMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@RequestMapping("/accounts")
public class AccountController extends ApiController<Account, AccountDto>{
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private static final String PAGE_PATH = "http://localhost:8080/accounts?page=";
    private static final String PATH = "http://localhost:8080/accounts/";

    public AccountController(AccountService accountService, AccountMapper accountMapper, PageMapper<Account, AccountDto> pageMapper) {
        super(pageMapper);
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDto create(@Valid @RequestBody AccountDto accountDto) {
        Account account = accountMapper.accountDtoToAccount(accountDto);
        Account createdAccount = accountService.create(account);
        return accountMapper.accountToAccountDto(createdAccount);
    }

    @GetMapping
    public PageDto<AccountDto> findAccounts(@RequestParam(defaultValue = "1") @Min(value = 1, message = "page number should be greater than zero") int page,
                                            @RequestParam(defaultValue = "3") @Min(value = 1, message = "size should be greater than zero") int size) {
        Page<Account> accountPage = accountService.findAll(page, size);
        Page<Account> filledPage = fillPageLinks(accountPage, page, size, PAGE_PATH);
        return convertPageToPageDto(filledPage, accountMapper::accountToAccountDto, PATH);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDto findAccountById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") Long id) {
        return accountService.findById(id, Account.class).map(accountMapper::accountToAccountDto)
                .orElseThrow(() -> new EntityNotFoundException("Account with such id is not found"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable @Valid @Min(value = 1, message = "Id should be greater than zero") Long id) {
        accountService.delete(id, Account.class);
    }
}
