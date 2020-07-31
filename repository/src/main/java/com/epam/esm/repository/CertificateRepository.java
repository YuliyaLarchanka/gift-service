package com.epam.esm.repository;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;

public interface CertificateRepository extends ApiRepository<Certificate, Long> {
    Page<Certificate> filterCertificatesByTagAndPrice(String tagName, String price);
    Page<Certificate> filterCertificatesByTagAndDescription(String tagName, String descriptionPart, String order, int page, int size);
}
