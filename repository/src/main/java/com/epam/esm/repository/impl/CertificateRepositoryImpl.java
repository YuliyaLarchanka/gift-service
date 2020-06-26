package com.epam.esm.repository.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.rowmapper.CertificateRowMapper;
import com.epam.esm.repository.rowmapper.TagRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TagRepository tagRepository;

    private final static String CREATE_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price," +
            " date_of_creation, duration_in_days) " +
            "values (?, ?, ?, ?, ?)";

    private final static String FIND_CERTIFICATE_TAGS = "SELECT * FROM tag as t JOIN gift_certificate_m2m_tag as m2m " +
            "ON m2m.tag_id = t.tag_id and m2m.gift_certificate_id = ?";

    private final static String INSERT_M2M = "INSERT INTO gift_certificate_m2m_tag (gift_certificate_id, tag_id) " +
            "values (?, ?)";

    private final static String FIND_ALL_CERTIFICATES = "SELECT gift_certificate_id, name, description, " +
            "price, date_of_creation, date_of_modification, duration_in_days FROM gift_certificate";

    private final static String FIND_CERTIFICATE_BY_ID = "SELECT gift_certificate_id, name, description," +
            " price, date_of_creation, date_of_modification, duration_in_days FROM gift_certificate " +
            "WHERE gift_certificate_id = ?";

    private final static String FIND_CERTIFICATE_BY_NAME = "SELECT gift_certificate_id, name, description," +
            " price, date_of_creation, date_of_modification, duration_in_days FROM gift_certificate WHERE name = ?";

    private final static String DELETE_TAGS_FROM_M2M = "DELETE FROM gift_certificate_m2m_tag " +
            "WHERE tag_id = ? and gift_certificate_id = ?";

    private final static String UPDATE_CERTIFICATE_SQL = "UPDATE gift_certificate " +
            "SET name = ?, description = ?, price = ?, date_of_modification = ?, duration_in_days = ? " +
            "WHERE gift_certificate_id = ?";

    private final static String GET_CERTIFICATE_DATE_OF_CREATION = "SELECT date_of_creation FROM gift_certificate WHERE gift_certificate_id = ?";

    private final static String DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE gift_certificate_id = ?";


    public CertificateRepositoryImpl(JdbcTemplate jdbcTemplate, TagRepository tagRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRepository = tagRepository;
    }

    @Override
    public Certificate create(Certificate certificate) {
        certificate = createGiftCertificateHelper(certificate);
        long certificateId = certificate.getId();
        List<Tag> tagList = certificate.getTagList();
        if (!tagList.isEmpty()) {
            tagList = tagList.stream()
                    .map(this::createTagIfNotExist)
                    .collect(Collectors.toList());

            tagList.forEach(tag -> jdbcTemplate.update(INSERT_M2M, certificateId, tag.getId()));
        }
        certificate.setTagList(tagList);
        return certificate;
    }

    private Tag createTagIfNotExist(Tag tag) {
        String name = tag.getName();
        Optional<Tag> optionalTag = tagRepository.findByName(name);
        if (optionalTag.isEmpty()) {
            return tagRepository.create(tag);
        }
        return optionalTag.get();
    }

    private Certificate createGiftCertificateHelper(Certificate certificate) {
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Timestamp dateOfCreation = Timestamp.valueOf(certificate.getDateOfCreation());
        short durationInDays = certificate.getDurationInDays();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(CREATE_CERTIFICATE, new String[]{"gift_certificate_id"});
                    ps.setString(1, name);
                    ps.setString(2, description);
                    ps.setBigDecimal(3, price);
                    ps.setTimestamp(4, dateOfCreation);
                    ps.setShort(5, durationInDays);
                    return ps;
                },
                keyHolder
        );
        certificate.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return certificate;
    }


    @Override
    public List<Certificate> findAll() {
        List<Certificate> certificateList = jdbcTemplate.query(FIND_ALL_CERTIFICATES, new CertificateRowMapper());
        return certificateList.stream().map(this::populateCertificateTags).collect(Collectors.toList());
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        try {
            Certificate certificate = jdbcTemplate.queryForObject(FIND_CERTIFICATE_BY_ID, new Object[]{id}, new CertificateRowMapper());
            return Optional.ofNullable(populateCertificateTags(certificate));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Certificate> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_CERTIFICATE_BY_NAME, new Object[]{name}, new CertificateRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Certificate update(Certificate certificate) {
        certificate = updateGiftCertificateHelper(certificate);
        long certificateId = certificate.getId();
        List<Tag> updatedTags = certificate.getTagList();
        List<Tag> oldTagList = jdbcTemplate.query(FIND_CERTIFICATE_TAGS, new Object[]{certificateId}, new TagRowMapper());

        List<Tag> tagsToAdd = updatedTags
                .stream()
                .filter(tag -> checkListNotContainsTag(oldTagList, tag))
                .collect(Collectors.toList());

        List<Tag> existingTagsToAdd = tagsToAdd
                .stream()
                .map(tag -> tagRepository.findByName(tag.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Tag> createdTagsToAdd = tagsToAdd
                .stream()
                .filter(tag -> checkListNotContainsTag(existingTagsToAdd, tag))
                .map(tagRepository::create)
                .collect(Collectors.toList());

        existingTagsToAdd.addAll(createdTagsToAdd);
        existingTagsToAdd.forEach(tag -> jdbcTemplate.update(INSERT_M2M, certificateId, tag.getId()));

        List<Tag> tagsToDelete = oldTagList
                .stream()
                .filter(tag -> checkListNotContainsTag(updatedTags, tag))
                .collect(Collectors.toList());

        if (!tagsToDelete.isEmpty()) {
            List<Object[]> batchArgs = tagsToDelete
                    .stream()
                    .map(tag -> new Object[]{tag.getId(), certificateId})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(DELETE_TAGS_FROM_M2M, batchArgs);
        }

        List<Tag> updatedTagList = jdbcTemplate.query(FIND_CERTIFICATE_TAGS, new Object[]{certificateId}, new TagRowMapper());
        certificate.setTagList(updatedTagList);
        return certificate;
    }

    private boolean checkListNotContainsTag(List<Tag> tagList, Tag tagToSearch) {
        return tagList.stream().noneMatch(tag -> tagToSearch.getName().equals(tag.getName()));
    }

    private Certificate updateGiftCertificateHelper(Certificate certificate) {
        long id = certificate.getId();
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        LocalDateTime dateOfModification = certificate.getDateOfModification();
        short durationInDays = certificate.getDurationInDays();

        Object[] params = {name, description, price, dateOfModification, durationInDays, id};

        jdbcTemplate.update(UPDATE_CERTIFICATE_SQL, params);
        LocalDateTime dateOfCreation = jdbcTemplate.queryForObject(GET_CERTIFICATE_DATE_OF_CREATION,
                new Object[]{id}, LocalDateTime.class);
        certificate.setDateOfCreation(dateOfCreation);
        return certificate;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }

    private Certificate populateCertificateTags(Certificate certificate) {
        List<Tag> tagList = jdbcTemplate.query(FIND_CERTIFICATE_TAGS, new Object[]{certificate.getId()}, new TagRowMapper());
        certificate.setTagList(tagList);
        return certificate;
    }

    @Override
    public List<Certificate> filterCertificates(String tagName, String textField, String order) {
        StringBuilder sql = new StringBuilder("SELECT g.gift_certificate_id, g.name, g.description, g.price," +
                " g.date_of_creation, g.date_of_modification, g.duration_in_days FROM ");

        if (textField != null) {
            sql.append("\"filter_by_text\"('").append(textField).append("') as g");
        } else {
            sql.append("gift_certificate as g");
        }

        if (tagName != null) {
            Optional<Tag> tagOptional = tagRepository.findByName(tagName);
            if (tagOptional.isPresent()) {
                long id = tagOptional.get().getId();
                sql.append(" JOIN gift_certificate_m2m_tag as m2m " +
                        "ON m2m.gift_certificate_id = g.gift_certificate_id and m2m.tag_id = ").append(id);
            }
        }

        if (order != null) {
            sql.append(" ORDER BY g.date_of_creation ").append(order.toUpperCase());
        }
        List<Certificate> certificateList = jdbcTemplate.query(sql.toString(), new CertificateRowMapper());
        return certificateList.stream().map(this::populateCertificateTags).collect(Collectors.toList());
    }
}
