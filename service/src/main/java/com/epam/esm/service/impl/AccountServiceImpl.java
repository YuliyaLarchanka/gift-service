package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.RoleEnum;
import com.epam.esm.service.AccountService;
import com.epam.esm.service.exception.DuplicateEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl extends ApiServiceImpl<Account, Long, AccountRepository> implements AccountService {
    public AccountServiceImpl(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    @Transactional
    public Account create(Account account) {
        Optional<Account> accountOptional = repository.findByName(account.getLogin());
        if (accountOptional.isPresent()){
            throw new DuplicateEntityException("Account with the same login already exists");
        }
        account.setRole(RoleEnum.CLIENT);
        return repository.create(account);
    }

    @Override
    public Optional<Account> update(Account order) {
        return Optional.of(new Account());
    }
}
