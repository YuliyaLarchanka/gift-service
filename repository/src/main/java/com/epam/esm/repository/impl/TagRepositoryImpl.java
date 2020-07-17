package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Tag> findById(Long id) {
        try {
            Tag tag = em.find(Tag.class, id);
            return Optional.ofNullable(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() {
        Query query = em.createQuery("select t from Tag t");
        return query.getResultList();
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

    public Tag createTagIfNotExist(Tag tag) {
        if(tag.getId()!=null){
            Optional<Tag> tagOptional = findById(tag.getId());
            if (tagOptional.isEmpty()){
                throw new RuntimeException();//TODO: create exception
            }

            Tag existedTag = tagOptional.get();
            if(!existedTag.getName().equals(tag.getName())){
                throw new RuntimeException();//TODO: create exception - tag name doesn't match with the existed tag name
            }
            return tag;
        }

        Optional<Tag> tagOptional = findByName(tag.getName());
        if (tagOptional.isEmpty()){
            return create(tag);
        }
        return tagOptional.get();
    }

    @Override
    public void delete(Tag tag) {
        em.remove(tag);
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        throw new UnsupportedOperationException();
    }
}
