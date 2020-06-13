package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepository {
    private final JdbcTemplate jdbcTemplate;

    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Tag> findAll() {
        String sql = "SELECT * FROM tag";
        return jdbcTemplate.query(sql, new TagRowMapper());
    }
}
