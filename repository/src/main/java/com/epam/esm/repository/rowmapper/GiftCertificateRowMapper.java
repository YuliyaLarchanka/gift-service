package com.epam.esm.repository.rowmapper;

import com.epam.esm.repository.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong("gift_certificate_id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getBigDecimal("price"));


        giftCertificate.setDateOfCreation(rs.getTimestamp("date_of_creation").toLocalDateTime());

        if (rs.getTimestamp("date_of_modification") != null) {
            giftCertificate.setDateOfModification(rs.getTimestamp("date_of_modification").toLocalDateTime());
        } else {
            giftCertificate.setDateOfModification(null);
        }

        giftCertificate.setDurationInDays(rs.getShort("duration_in_days"));
        return giftCertificate;
    }
}
