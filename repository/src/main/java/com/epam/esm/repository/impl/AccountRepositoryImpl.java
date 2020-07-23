package com.epam.esm.repository.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Page;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Account create(Account account) {
        em.persist(account);
        return account;
    }

    @Override
    public Optional<Account> findById(Long id) {
        try {
            Account account = em.find(Account.class, id);
            return Optional.ofNullable(account);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page findAll(int page, int size) {
        return new Page();
    }

    @Override
    public Optional<Account> update(Account account) {
        return Optional.of(new Account());
    }

    @Override
    public void delete(Account order) {
        //
    }
}
