package com.epam.esm.repository.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl extends ApiRepositoryImpl<Account, Long> implements AccountRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected String getClassName() {
        return "Account";
    }

    @Override
    public Account create(Account account) {
        em.persist(account);
        return account;
    }

    @Override
    public Optional<Account> findByLogin(String login){
        Query query = em.createQuery("select t from Account t where t.login = ?1", Account.class);
        try {
            Account account = (Account) query.setParameter(1, login).getSingleResult();
            return Optional.of(account);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> update(Account account) {
        return Optional.of(new Account());
    }
}
