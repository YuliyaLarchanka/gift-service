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

    @Override
    public Page<Certificate> filterCertificatesByTagAndDescription(List<String> tagNames, String descriptionPart, String order, int page, int size) {
        StringBuilder sql = new StringBuilder("SELECT c.certificate_id, c.name, c.description, c.price," +
                " c.date_of_creation, c.date_of_modification, c.duration_in_days, t.is_deleted FROM ");

        if (descriptionPart != null) {
            sql.append("filter_by_text('").append(descriptionPart).append("') as c");
        } else {
            sql.append("certificate as c");
        }

        if (tagNames.size() > 0) {

            int tagNamesSize = tagNames.size()-1;

           sql.append(" JOIN certificate_m2m_tag as m2m " +
                        "ON m2m.certificate_id = c.certificate_id join tag t " +
                   "on m2m.tag_id = t.tag_id where t.name in (");

           for (String name : tagNames) {
                sql.append("'" + name + "'");
                if (tagNames.indexOf(name)!=tagNamesSize){
                   sql.append(", ");
                }else{
                    sql.append(")");
                }
            }
            }

        if (order != null) {
            sql.append(" ORDER BY c.date_of_creation ").append(order.toUpperCase());
        }

        Query query = em.createNativeQuery(sql.toString(), Certificate.class);
        List totalList = em.createNativeQuery(sql.toString(), Certificate.class).getResultList();
        int totalCount = totalList.size();
        Page<Certificate> filledPage = fillPage(page, size, totalCount);
        List<Certificate> certificatesPerPage = query.setFirstResult(filledPage.getOffset()).setMaxResults(size).getResultList();
        filledPage.setContent(certificatesPerPage);
        return filledPage;
    }
}
