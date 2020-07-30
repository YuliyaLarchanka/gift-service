package com.epam.esm.service;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.service.security.AuthorisedAccount;

import java.util.Optional;

public interface AccountService extends ApiService<Account, Long>{
    Optional<Account> findByLoginAndPassword(String login, String password);

    Optional<Account> findByLogin(String login);
}
