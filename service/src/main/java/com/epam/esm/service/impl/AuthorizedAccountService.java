package com.epam.esm.service.impl;

import com.epam.esm.service.AccountService;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.security.AuthorisedAccount;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorizedAccountService implements UserDetailsService {
    private final AccountService accountService;

    public AuthorizedAccountService(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public AuthorisedAccount loadUserByUsername(String username) {
        Optional<AuthorisedAccount> authorisedAccountOptional = accountService
                .findByLogin(username)
                .map(AuthorisedAccount::fromUserEntityToCustomUserDetails);

        if (authorisedAccountOptional.isEmpty()){
            throw new EntityNotFoundException("Account doesn't found");
        }

        return authorisedAccountOptional.get();
    }
}
