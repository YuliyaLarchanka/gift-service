package com.epam.esm.repository.rowmapper;

import com.epam.esm.repository.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong("tag_id"));
        tag.setName(rs.getString("name"));
        return tag;
    }
}
