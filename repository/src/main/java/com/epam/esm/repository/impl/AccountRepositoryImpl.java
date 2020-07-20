package com.epam.esm.repository.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
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
    public List<Account> findAll() {
        return new ArrayList<>();
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
