package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.repository.entity.RoleEnum;
import com.epam.esm.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Account create(Account account) {
        account.setRole(RoleEnum.CLIENT);
        return accountRepository.create(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.of(new Account());

    }

    @Override
    public Page<Account> findAll(int page, int size) {
        return new Page<>();
    }

    @Override
    public Optional<Account> update(Account order) {
        return Optional.of(new Account());
    }

    @Override
    public void delete(Long id) {
        //
    }
}
