package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class TagRepositoryImpl extends ApiRepositoryImpl<Tag, Long> implements TagRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected String getClassName() {
        return "Tag";
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Query query = em.createQuery("select t from Tag t where t.name = ?1", Tag.class);
        try {
            Tag tag = (Tag) query.setParameter(1, name).getSingleResult();
            return Optional.of(tag);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tag create(Tag tag) {
        em.persist(tag);
        return tag;
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        throw new UnsupportedOperationException();
    }
}
