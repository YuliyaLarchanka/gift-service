package com.epam.esm.repository;

import com.epam.esm.repository.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends ApiRepository<GiftCertificate, Long>{
    List<GiftCertificate> filterCertificates(StringBuilder sql);
}
