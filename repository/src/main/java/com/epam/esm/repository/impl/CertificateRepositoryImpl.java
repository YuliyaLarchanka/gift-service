package com.epam.esm.repository.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl extends ApiRepositoryImpl<Certificate, Long> implements CertificateRepository{
    private static final String SELECT_BY_TAG_AND_PRICE_JPQL = "select c from Certificate" +
            " c JOIN c.tagList t ON t.name IN :names order by c.price ";

    private final TagRepository tagRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    protected String getClassName() {
        return "Certificate";
    }

    public CertificateRepositoryImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Certificate create(Certificate certificate) {
        em.persist(certificate);
        return certificate;
    }

    @Transactional
    @Override
    public Optional<Certificate> update(Certificate certificate) {
        return Optional.of(em.merge(certificate));
    }

    public Page<Certificate> filterCertificatesByTagAndPrice(List<String> tagNames, String price){
        String jpql = SELECT_BY_TAG_AND_PRICE_JPQL + price;
        Query query = em.createQuery(jpql, Certificate.class);
        Page<Certificate> page = new Page<>();
        try {
            Certificate certificate = (Certificate)query.setParameter("names", tagNames).setMaxResults(1).getSingleResult();
            List<Certificate> certificates = new ArrayList<>();
            certificates.add(certificate);
            page.setContent(certificates);
        } catch (NoResultException e) {
            return page;
        }
        return page;
    }
}
