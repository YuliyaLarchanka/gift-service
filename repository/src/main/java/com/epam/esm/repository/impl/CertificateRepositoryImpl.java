package com.epam.esm.repository.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {
    private final TagRepository tagRepository;

    @PersistenceContext
    private EntityManager em;

    public CertificateRepositoryImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Certificate create(Certificate certificate) {
        List<Tag> tagList = certificate.getTagList();
        if (!tagList.isEmpty()) {
            tagList = tagList.stream()
                    .map(tagRepository::createTagIfNotExist)
                    .collect(Collectors.toList());
        }
        certificate.setTagList(tagList);
        em.persist(certificate);
        return certificate;
    }

    @Override
    public List<Certificate> findAll() {
        Query query = em.createQuery("select c from Certificate c");
        return query.getResultList();
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        try {
            Certificate certificate = em.find(Certificate.class, id);
            return Optional.ofNullable(certificate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Certificate> findByName(String name) {
        Query query = em.createQuery("select c from Certificate c where c.name = ?1", Certificate.class);
        try {
            Certificate certificate = (Certificate) query.setParameter(1, name).getSingleResult();
            return Optional.of(certificate);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<Certificate> update(Certificate certificate) {
        return Optional.of(em.merge(certificate));
    }

    @Override
    public void delete(Certificate certificate) {
        em.remove(certificate);
    }

    @Override
    public List<Certificate> filterCertificates(String tagName, String descriptionPart, String order) {
        StringBuilder sql = new StringBuilder("SELECT g.gift_certificate_id, g.name, g.description, g.price," +
                " g.date_of_creation, g.date_of_modification, g.duration_in_days FROM ");

        if (descriptionPart != null) {
            sql.append("\"filter_by_text\"('").append(descriptionPart).append("') as g");
        } else {
            sql.append("gift_certificate as g");
        }

        if (tagName != null) {
            Optional<Tag> tagOptional = tagRepository.findByName(tagName);
            if (tagOptional.isPresent()) {
                long id = tagOptional.get().getId();
                sql.append(" JOIN gift_certificate_m2m_tag as m2m " +
                        "ON m2m.gift_certificate_id = g.gift_certificate_id and m2m.tag_id = ").append(id);
            } else {
                return Collections.emptyList();
            }
        }

        if (order != null) {
            sql.append(" ORDER BY g.date_of_creation ").append(order.toUpperCase());
        }

        return em.createQuery(sql.toString()).getResultList();
    }
}
