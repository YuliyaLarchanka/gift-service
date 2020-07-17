package com.epam.esm.repository.rowmapper;

import com.epam.esm.repository.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateRowMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong("gift_certificate_id"));
        certificate.setName(rs.getString("name"));

        certificate.setDescription(rs.getString("description"));

        certificate.setPrice(rs.getBigDecimal("price"));
        certificate.setDateOfCreation(rs.getTimestamp("date_of_creation").toLocalDateTime());

        if (rs.getTimestamp("date_of_modification") != null) {
            certificate.setDateOfModification(rs.getTimestamp("date_of_modification").toLocalDateTime());
        } else {
            certificate.setDateOfModification(null);
        }
        certificate.setDurationInDays(rs.getShort("duration_in_days"));
        return certificate;
    }
}
