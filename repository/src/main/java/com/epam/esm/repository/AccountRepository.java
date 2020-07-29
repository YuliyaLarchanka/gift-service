package com.epam.esm.repository;

import com.epam.esm.repository.entity.Account;

import java.util.Optional;

public interface AccountRepository extends ApiRepository<Account, Long>{
    Optional<Account> findByName(String name);
}
