package com.epam.esm.repository;

import com.epam.esm.repository.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends ApiRepository<Certificate, Long> {
    Optional<Certificate> findByName(String name);

    Optional<Certificate> filterCertificatesByTagAndPrice(String tagName, String price);
    List<Certificate> filterCertificatesByTagAndDescription(String tagName, String descriptionPart, String order);

}
