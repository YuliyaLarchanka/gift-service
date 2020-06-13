package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;


public interface GiftCertificateService extends ApiService<GiftCertificateDto, Long>{
    List<GiftCertificateDto> filterCertificates(String tagName, String textField, String order);
}
