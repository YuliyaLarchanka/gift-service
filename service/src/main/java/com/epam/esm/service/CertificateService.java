package com.epam.esm.service;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CertificatePatchDto;

import java.util.List;
import java.util.Optional;


public interface CertificateService extends ApiService<CertificateDto, Long> {
    CertificateDto convertCertificateToDto(Certificate certificate);

    List<CertificateDto> filterCertificates(String tagName, String descriptionPart, String order);

    Optional<CertificateDto> updateOneField(CertificatePatchDto certificatePatchDto);
}
