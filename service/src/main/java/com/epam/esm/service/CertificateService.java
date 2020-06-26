package com.epam.esm.service;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.service.dto.CertificateDto;

import java.util.List;


public interface CertificateService extends ApiService<CertificateDto, Long> {
    CertificateDto convertCertificateToDto(Certificate certificate);

    List<CertificateDto> filterCertificates(String tagName, String textField, String order);
}
