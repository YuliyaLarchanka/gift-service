package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.rowmapper.TagRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final JdbcTemplate jdbcTemplate;

    private final static String FIND_TAG_BY_ID = "SELECT tag_id, name FROM tag WHERE tag_id = ?";

    private final static String FIND_ALL_TAGS = "SELECT tag_id, name FROM tag";

    private final static String FIND_TAG_BY_NAME = "SELECT tag_id, name FROM tag WHERE name = ?";

    private final static String CREATE_TAG = "INSERT INTO tag (name) values (?)";

    private final static String DELETE_TAG = "DELETE FROM tag WHERE tag_id = ?";

    public TagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_TAG_BY_ID, new Object[]{id}, new TagRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL_TAGS, new TagRowMapper());
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_TAG_BY_NAME, new Object[]{name}, new TagRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tag create(Tag tag) {
        String name = tag.getName();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(CREATE_TAG, new String[]{"tag_id"});
                    ps.setString(1, name);
                    return ps;
                },
                keyHolder
        );
        tag.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return tag;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException();
    }
}
