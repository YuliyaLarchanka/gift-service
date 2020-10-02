package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.RoleEnum;
import com.epam.esm.service.AccountService;
import com.epam.esm.service.exception.DuplicateEntityException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl extends ApiServiceImpl<Account, Long, AccountRepository> implements AccountService {
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        super(accountRepository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Account create(Account account) {
        Optional<Account> accountOptional = repository.findByLogin(account.getLogin());
        if (accountOptional.isPresent()){
            throw new DuplicateEntityException("Account with the same login already exists");
        }
        Account accountToCreate = fillAccount(account);
        return repository.create(accountToCreate);
    }

    private Account fillAccount(Account account){
        String rawPassword = account.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        account.setPassword(encodedPassword);
        account.setRole(RoleEnum.ROLE_CLIENT);
        return account;
    }

    @Override
    public Optional<Account> update(Account account) {
       throw new UnsupportedOperationException();

    }

    public Optional<Account> findByLoginAndPassword(String login, String password) {
        return findByLogin(login).filter(account -> passwordMatches(account, password));
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    private boolean passwordMatches(Account account, String password) {
        String accountPassword = account.getPassword();
        return passwordEncoder.matches(password, accountPassword);
    }
}
