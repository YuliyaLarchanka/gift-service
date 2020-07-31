package com.epam.esm.service;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;

import java.util.Optional;


public interface CertificateService extends ApiService<Certificate, Long> {
    Page<Certificate> filterCertificatesByTagAndPrice(String tagName, String price);

    Page<Certificate> filterCertificatesByTagAndDescription(String tagName, String descriptionPart, String order, int page, int size);

    Optional<Certificate> updateOneField(Certificate certificate);
}
