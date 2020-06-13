package com.epam.esm.repository.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.rowmapper.GiftCertificateRowMapper;
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
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
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

    private final static String DELETE_TAGS_FROM_M2M = "DELETE FROM gift_certificate_m2m_tag " +
            "WHERE tag_id = ? and gift_certificate_id = ?";

    private final static String UPDATE_CERTIFICATE_SQL = "UPDATE gift_certificate " +
            "SET name = ?, description = ?, price = ?, date_of_modification = ?, duration_in_days = ? " +
            "WHERE gift_certificate_id = ?";

    private final static String GET_CERTIFICATE_DATE_OF_CREATION = "SELECT date_of_creation FROM gift_certificate WHERE gift_certificate_id = ?";

    private final static String DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE gift_certificate_id = ?";


    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate, TagRepository tagRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRepository = tagRepository;
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        giftCertificate = createGiftCertificateHelper(giftCertificate);
        long certificateId = giftCertificate.getId();
        List<Tag> tagList = giftCertificate.getTagList();

        if (!tagList.isEmpty()) {
            tagList = tagList.stream()
                    .map(this::createTagIfNotExist)
                    .collect(Collectors.toList());

            tagList.forEach(tag -> jdbcTemplate.update(INSERT_M2M, certificateId, tag.getId()));
        }
        giftCertificate.setTagList(tagList);
        return giftCertificate;
    }

    private Tag createTagIfNotExist(Tag tag) {
        String name = tag.getName();
        Optional<Tag> optionalTag = tagRepository.findTagByName(name);
        if (optionalTag.isEmpty()) {
            return tagRepository.create(tag);
        }
        return optionalTag.get();
    }

    private GiftCertificate createGiftCertificateHelper(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        BigDecimal price = giftCertificate.getPrice();
        Timestamp dateOfCreation = Timestamp.valueOf(giftCertificate.getDateOfCreation());
        short durationInDays = giftCertificate.getDurationInDays();

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
        giftCertificate.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return giftCertificate;
    }


    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> giftCertificateList = jdbcTemplate.query(FIND_ALL_CERTIFICATES, new GiftCertificateRowMapper());
        return giftCertificateList.stream().map(this::populateCertificateTags).collect(Collectors.toList());
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        try {
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_CERTIFICATE_BY_ID, new Object[]{id}, new GiftCertificateRowMapper());
            return Optional.ofNullable(populateCertificateTags(giftCertificate));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        giftCertificate = updateGiftCertificateHelper(giftCertificate);

        long certificateId = giftCertificate.getId();
        List<Tag> updatedTags = giftCertificate.getTagList();

        List<Tag> oldTagList = jdbcTemplate.query(FIND_CERTIFICATE_TAGS, new Object[]{certificateId}, new TagRowMapper());

        List<Tag> tagsToAdd = updatedTags
                .stream()
                .filter(tag -> checkListNotContainsTag(oldTagList, tag))
                .collect(Collectors.toList());

        List<Tag> existingTagsToAdd = tagsToAdd
                .stream()
                .map(tag -> tagRepository.findTagByName(tag.getName()))
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
        giftCertificate.setTagList(updatedTagList);
        return giftCertificate;
    }

    private boolean checkListNotContainsTag(List<Tag> tagList, Tag tagToSearch) {
        return tagList.stream().noneMatch(tag -> tagToSearch.getName().equals(tag.getName()));
    }

    private GiftCertificate updateGiftCertificateHelper(GiftCertificate giftCertificate) {
        long id = giftCertificate.getId();
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        BigDecimal price = giftCertificate.getPrice();
        LocalDateTime dateOfModification = giftCertificate.getDateOfModification();
        short durationInDays = giftCertificate.getDurationInDays();

        Object[] params = {name, description, price, dateOfModification, durationInDays, id};

        jdbcTemplate.update(UPDATE_CERTIFICATE_SQL, params);
        LocalDateTime dateOfCreation = jdbcTemplate.queryForObject(GET_CERTIFICATE_DATE_OF_CREATION,
                new Object[]{id}, LocalDateTime.class);
        giftCertificate.setDateOfCreation(dateOfCreation);
        return giftCertificate;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }

    private GiftCertificate populateCertificateTags(GiftCertificate giftCertificate) {
        List<Tag> tagList = jdbcTemplate.query(FIND_CERTIFICATE_TAGS, new Object[]{giftCertificate.getId()}, new TagRowMapper());
        giftCertificate.setTagList(tagList);
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> filterCertificates(StringBuilder sql) {
        List<GiftCertificate> giftCertificateList = jdbcTemplate.query(sql.toString(), new GiftCertificateRowMapper());
        return giftCertificateList.stream().map(this::populateCertificateTags).collect(Collectors.toList());
    }
}
