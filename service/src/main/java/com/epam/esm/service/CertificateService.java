package com.epam.esm.service;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;

import java.util.List;
import java.util.Optional;


public interface CertificateService extends ApiService<Certificate, Long> {
    Page<Certificate> filterCertificatesByTagAndPrice(List<String> tagNames, String price);

    Page<Certificate> filterCertificatesByTagAndDescription(List<String> tagNames, String descriptionPart, String order, int page, int size);

    Optional<Certificate> updateOneField(Certificate certificate);
}
