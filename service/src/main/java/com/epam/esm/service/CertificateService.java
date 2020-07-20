package com.epam.esm.service;

import com.epam.esm.repository.entity.Certificate;

import java.util.List;
import java.util.Optional;


public interface CertificateService extends ApiService<Certificate, Long> {
    List<Certificate> filterCertificates(String tagName, String descriptionPart, String order);

    Optional<Certificate> updateOneField(Certificate certificate);
}
