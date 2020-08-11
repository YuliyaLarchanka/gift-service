package com.epam.esm.repository;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;

import java.util.List;

public interface CertificateRepository extends ApiRepository<Certificate, Long> {
    Page<Certificate> filterCertificatesByTagAndPrice(List<String> tagNames, String price);
    Page<Certificate> filterCertificatesByTagAndDescription(List<String> tagNames, String descriptionPart, String order, int page, int size);
}
